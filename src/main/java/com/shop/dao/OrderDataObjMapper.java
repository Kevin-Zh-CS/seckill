package com.shop.dao;

import com.alibaba.druid.sql.PagerUtils;
import com.shop.dao.dataobject.OrderDataObj;
import com.shop.util.PageQueryUtil;

import java.util.List;

public interface OrderDataObjMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sun Sep 20 21:34:48 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sun Sep 20 21:34:48 CST 2020
     */
    int insert(OrderDataObj record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sun Sep 20 21:34:48 CST 2020
     */
    int insertSelective(OrderDataObj record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sun Sep 20 21:34:48 CST 2020
     */
    OrderDataObj selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sun Sep 20 21:34:48 CST 2020
     */
    int updateByPrimaryKeySelective(OrderDataObj record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sun Sep 20 21:34:48 CST 2020
     */
    int updateByPrimaryKey(OrderDataObj record);


    int getTotalOrders(PageQueryUtil pageUtil);

    List<OrderDataObj> getTotalOrderList(PageQueryUtil pageQueryUtil);

    List<OrderDataObj> getAllOrders();
}