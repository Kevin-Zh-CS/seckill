package com.shop.service.impl;

import com.shop.controller.viewobject.OrderView;
import com.shop.dao.ItemDataObjMapper;
import com.shop.dao.OrderDataObjMapper;
import com.shop.dao.SequenceDataObjMapper;
import com.shop.dao.dataobject.OrderDataObj;
import com.shop.dao.dataobject.SequenceDataObj;
import com.shop.error.BusinessError;
import com.shop.error.BusinessException;
import com.shop.service.ItemService;
import com.shop.service.OrderService;
import com.shop.service.UserService;
import com.shop.service.model.ItemModel;
import com.shop.service.model.OrderModel;
import com.shop.service.model.UserModel;
import com.shop.util.PageQueryUtil;
import com.shop.util.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDataObjMapper orderDataObjMapper;

    @Autowired
    private SequenceDataObjMapper sequenceDataObjMapper;

    @Autowired
    private ItemDataObjMapper itemDataObjMapper;


    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        //校验下单状态：下单商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(BusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(BusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }
        if (amount <= 0 || amount > 99) {
            throw new BusinessException(BusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不正确");
        }
        //校验活动信息
        if (promoId != null) {
            if (promoId.intValue() != itemModel.getPromoModel().getId()) {
                throw new BusinessException(BusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
            } else if (itemModel.getPromoModel().getStatus() != 2) {
                throw new BusinessException(BusinessError.PARAMETER_VALIDATION_ERROR, "活动未开始");
            }
        }
        //落单减库存，而不是支付减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(BusinessError.STOCK_NOT_ENOUTH);
        }
        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if (promoId != null) {
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        orderModel.setId(generateOrderNumber());
        orderModel.setPromoId(promoId);
        OrderDataObj orderDataObj = convertFromModelToOrderDataObj(orderModel);
        orderDataObjMapper.insertSelective(orderDataObj);
        itemService.increaseSales(itemId, amount);
        //返回前端
        return orderModel;
    }

    @Override
    public PageResult getOrders(PageQueryUtil pageUtil) {
        int total = orderDataObjMapper.getTotalOrders(pageUtil);
        List<OrderDataObj> orderDataObjList = orderDataObjMapper.getTotalOrderList(pageUtil);
        List<OrderView> orderViewList = new ArrayList<>();
        for (OrderDataObj orderDataObj : orderDataObjList) {
            String imgUrl = itemDataObjMapper.selectByPrimaryKey(orderDataObj.getItemId()).getImgUrl();
            OrderView orderView = new OrderView();
            orderView.setImgUrl(imgUrl);
            BeanUtils.copyProperties(orderDataObj, orderView);
            orderViewList.add(orderView);
        }
        return new PageResult(orderViewList, total, pageUtil.getLimit(), pageUtil.getPage());
    }

    @Override
    public List<OrderDataObj> getAllOrders() {
        return orderDataObjMapper.getAllOrders();
    }

    @Override
    public void deleteOrder(String orderId) {
        orderDataObjMapper.deleteByPrimaryKey(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOrderNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowData = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowData);
        int sequence;
        SequenceDataObj sequenceDataObj = sequenceDataObjMapper.getSequenceByName("order_info");
        sequence = sequenceDataObj.getCurrentValue();
        sequenceDataObj.setCurrentValue(sequenceDataObj.getCurrentValue() + sequenceDataObj.getStep());
        sequenceDataObjMapper.updateByPrimaryKey(sequenceDataObj);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private OrderDataObj convertFromModelToOrderDataObj(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderDataObj orderDataObj = new OrderDataObj();
        BeanUtils.copyProperties(orderModel, orderDataObj);
        return orderDataObj;
    }
}
