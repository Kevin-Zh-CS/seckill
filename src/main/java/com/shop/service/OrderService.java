package com.shop.service;

import com.shop.dao.dataobject.OrderDataObj;
import com.shop.error.BusinessException;
import com.shop.service.model.OrderModel;
import com.shop.util.PageQueryUtil;
import com.shop.util.PageResult;

import java.util.List;

public interface OrderService {
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;

    PageResult getOrders(PageQueryUtil pageQueryUtil);

    List<OrderDataObj> getAllOrders();

    void deleteOrder(String orderId);
}
