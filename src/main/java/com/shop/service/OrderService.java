package com.shop.service;

import com.shop.error.BusinessException;
import com.shop.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}
