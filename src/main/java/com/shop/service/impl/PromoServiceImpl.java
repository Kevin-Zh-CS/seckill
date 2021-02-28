package com.shop.service.impl;

import com.shop.dao.PromoDataObjMapper;
import com.shop.dao.dataobject.PromoDataObj;
import com.shop.service.ItemService;
import com.shop.service.PromoService;
import com.shop.service.model.ItemModel;
import com.shop.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDataObjMapper promoDataObjMapper;

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private ItemService itemService;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        PromoDataObj promoDataObj = promoDataObjMapper.selectByItemId(itemId);
        //service层需要model，而不是dataobj
        PromoModel promoModel = convertFromDataObjToPromoModel(promoDataObj);
        if (promoModel == null) {
            return null;
        }
        //判断当前时间是否秒杀活动即将开始或正在进行
        //DateTime noww = new DateTime();
        if (promoModel.getStartDate().isAfterNow()) {
            promoModel.setStatus(1);
        } else if (promoModel.getEndDate().isBeforeNow()) {
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    public void publishPromo(Integer promoId) {
        //通过活动id获取活动
        PromoDataObj promoDataObj = promoDataObjMapper.selectByPrimaryKey(promoId);
        if (promoDataObj.getItemId() == null || promoDataObj.getItemId() == 0)
            return;
        ItemModel itemModel = itemService.getItemById(promoDataObj.getItemId());
        //库存同步到Redis
        redisTemplate.opsForValue().set("promo_item_stock_" + itemModel.getId(), itemModel.getStock());
    }


    private PromoModel convertFromDataObjToPromoModel(PromoDataObj promoDataObj) {
        if (promoDataObj == null) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDataObj, promoModel);
        promoModel.setStartDate(new DateTime(promoDataObj.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDataObj.getEndDate()));
        return promoModel;
    }

}
