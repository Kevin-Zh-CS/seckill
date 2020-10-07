package com.shop.controller;

import com.shop.error.BusinessError;
import com.shop.error.BusinessException;
import com.shop.response.CommonReturnType;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonReturnType doError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception ex) {
        ex.printStackTrace();
        Map<String,Object> responseData = new HashMap<>();
        if( ex instanceof BusinessException){
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errCode",businessException.getErrorCode());
            responseData.put("errMessage",businessException.getErrorMessage());
        }else if(ex instanceof ServletRequestBindingException){
            responseData.put("errCode",BusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errMessage","url绑定路由问题");//必传的参数没有传触发
        }else if(ex instanceof NoHandlerFoundException){
            responseData.put("errCode",BusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errMessage","没有找到对应的访问路径");
        }else{
            responseData.put("errCode", BusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errMessage",BusinessError.UNKNOWN_ERROR.getErrorMessage());
        }
        return CommonReturnType.create(responseData,"fail");
    }
}