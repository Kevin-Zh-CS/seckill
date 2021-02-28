package com.shop.service;

import com.shop.dao.dataobject.UserDataObj;
import com.shop.error.BusinessException;
import com.shop.service.model.UserModel;

import java.util.List;

public interface UserService {

    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
    UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException;
    List<UserDataObj> getAllUsers();
}
