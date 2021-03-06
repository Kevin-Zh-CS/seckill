package com.shop.service.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderModel implements Serializable {
    //订单号
    private String id;
    //购买的用户ID
    private Integer userId;
    //购买的商品ID
    private Integer itemId;
    //购买商品的单价
    private BigDecimal itemPrice;
    //购买的数量
    private Integer amount;
    //购买金额
    private BigDecimal orderPrice;
    //非空则表示以秒杀商品的方式下单
    private Integer promoId;
    //商品图片
    private String imgUrl;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
