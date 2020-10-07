package com.shop.service.impl;

import com.shop.dao.UserDataObjMapper;
import com.shop.dao.UserPasswordDataObjMapper;
import com.shop.dao.dataobject.UserDataObj;
import com.shop.dao.dataobject.UserPasswordDataObj;
import com.shop.error.BusinessError;
import com.shop.error.BusinessException;
import com.shop.service.UserService;
import com.shop.service.model.UserModel;
import com.shop.validator.ValidationResult;
import com.shop.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDataObjMapper userDataObjMapper;

    @Autowired
    private UserPasswordDataObjMapper userPasswordDataObjMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDataObj userDataObj = userDataObjMapper.selectByPrimaryKey(id);
        UserPasswordDataObj userPasswordDataObj = userPasswordDataObjMapper.selectByUserId(id);
        if (userDataObj == null) {
            return null;
        }
        return convertFromObjectToModel(userDataObj, userPasswordDataObj);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(BusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //这种校验很不正常
//        if (StringUtils.isEmpty(userModel.getName())
//                || userModel.getGender() == null
//                || userModel.getAge() == null
//                || StringUtils.isEmpty(userModel.getTelphone())) {
//            throw new BusinessException(BusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        //正常校验
        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(BusinessError.PARAMETER_VALIDATION_ERROR,result.getErrorMsg());
        }

        UserDataObj userDataObj = convertFromModelToDataObj(userModel);

        try {
            userDataObjMapper.insertSelective(userDataObj);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(BusinessError.PARAMETER_VALIDATION_ERROR, "手机号已重复注册");
        }

        userModel.setId(userDataObj.getId());
        UserPasswordDataObj userPasswordDataObj = convertFromModelToPasswordDataObj(userModel);
        userPasswordDataObjMapper.insertSelective(userPasswordDataObj);
        return;

    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户手机获取用户信息
        UserDataObj userDataObj = userDataObjMapper.selectByTelphone(telphone);
        if(userDataObj == null){
            throw new BusinessException(BusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDataObj userPasswordDataObj = userPasswordDataObjMapper.selectByUserId(userDataObj.getId());
        UserModel userModel = convertFromObjectToModel(userDataObj, userPasswordDataObj);
        //比对密码
        if(!StringUtils.equals(encrptPassword, userPasswordDataObj.getEncrptPassword())){
            throw new BusinessException(BusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserModel convertFromObjectToModel(UserDataObj userDataObj, UserPasswordDataObj userPasswordDataObj) {
        if (userDataObj == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDataObj, userModel);
        if (userPasswordDataObj != null) {
            userModel.setEncrptPassword(userPasswordDataObj.getEncrptPassword());
        }
        return userModel;
    }

    private UserDataObj convertFromModelToDataObj(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserDataObj userDataObj = new UserDataObj();
        BeanUtils.copyProperties(userModel, userDataObj);
        return userDataObj;
    }

    private UserPasswordDataObj convertFromModelToPasswordDataObj(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserPasswordDataObj userPasswordDataObj = new UserPasswordDataObj();
        userPasswordDataObj.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDataObj.setUserId(userModel.getId());
        return userPasswordDataObj;
    }
}
