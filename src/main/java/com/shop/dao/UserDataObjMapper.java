package com.shop.dao;

import com.shop.dao.dataobject.UserDataObj;

public interface UserDataObjMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sun Sep 13 18:58:36 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sun Sep 13 18:58:36 CST 2020
     */
    int insert(UserDataObj record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sun Sep 13 18:58:36 CST 2020
     */
    int insertSelective(UserDataObj record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sun Sep 13 18:58:36 CST 2020
     */
    UserDataObj selectByPrimaryKey(Integer id);

    UserDataObj selectByTelphone(String telphone);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sun Sep 13 18:58:36 CST 2020
     */
    int updateByPrimaryKeySelective(UserDataObj record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sun Sep 13 18:58:36 CST 2020
     */
    int updateByPrimaryKey(UserDataObj record);
}