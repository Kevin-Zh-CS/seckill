package com.shop.service.impl;

import com.shop.dao.ItemDataObjMapper;
import com.shop.dao.ItemStockDataObjMapper;
import com.shop.dao.dataobject.ItemDataObj;
import com.shop.dao.dataobject.ItemStockDataObj;
import com.shop.error.BusinessError;
import com.shop.error.BusinessException;
import com.shop.service.ItemService;
import com.shop.service.PromoService;
import com.shop.service.model.ItemModel;
import com.shop.service.model.PromoModel;
import com.shop.validator.ValidationResult;
import com.shop.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDataObjMapper itemDataObjMapper;

    @Autowired
    private ItemStockDataObjMapper itemStockDataObjMapper;

    @Autowired
    private PromoService promoService;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors()) {
            throw new BusinessException(BusinessError.PARAMETER_VALIDATION_ERROR, result.getErrorMsg());
        }
        //转化model->dataobj
        ItemDataObj itemDataObj = convertFromModelToDataObject(itemModel);

        //写入数据库
        itemDataObjMapper.insertSelective(itemDataObj);
        itemModel.setId(itemDataObj.getId());
        ItemStockDataObj itemStockDataObj = this.convertFromModelToStockDataObject(itemModel);
        itemStockDataObjMapper.insertSelective(itemStockDataObj);

        //返回创建的对象
        return getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDataObj> itemDataObjList = itemDataObjMapper.listItem();
        List<ItemModel> itemModelList = itemDataObjList.stream().map(itemDataObj -> {
            ItemStockDataObj itemStockDataObj = itemStockDataObjMapper.selectByItemId(itemDataObj.getId());
            ItemModel itemModel = this.convertFromDataObjToModel(itemDataObj, itemStockDataObj);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDataObj itemDataObj = itemDataObjMapper.selectByPrimaryKey(id);
        if (itemDataObj == null) {
            return null;
        }
        //操作获得库存数量
        ItemStockDataObj itemStockDataObj = itemStockDataObjMapper.selectByItemId(itemDataObj.getId());

        //dataobject -> model
        ItemModel itemModel = convertFromDataObjToModel(itemDataObj, itemStockDataObj);

        //获取商品活动信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if (promoModel != null && promoModel.getStatus() != 3) {
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int affectedRows = itemStockDataObjMapper.decreaseStock(itemId, amount);
        if (affectedRows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDataObjMapper.increaseSales(itemId, amount);
    }

    private ItemDataObj convertFromModelToDataObject(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemDataObj itemDataObj = new ItemDataObj();
        BeanUtils.copyProperties(itemModel, itemDataObj);
        return itemDataObj;
    }

    private ItemStockDataObj convertFromModelToStockDataObject(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStockDataObj itemStockDataObj = new ItemStockDataObj();
        itemStockDataObj.setItemId(itemModel.getId());
        itemStockDataObj.setStock(itemModel.getStock());
        return itemStockDataObj;
    }

    private ItemModel convertFromDataObjToModel(ItemDataObj itemDataObj, ItemStockDataObj itemStockDataObj) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDataObj, itemModel);
        itemModel.setStock(itemStockDataObj.getStock());
        return itemModel;
    }
}
