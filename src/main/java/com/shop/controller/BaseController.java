package com.shop.controller;

/*
功能已被GlobalExceptionHandler取代
 */
public class BaseController {

    public static final String CONTENT_TYPE_FROMED = "application/x-www-form-urlencoded";
    //定义exceptionhandler解决未被controller层吸收的exception

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Object handlerException(HttpServletRequest request, Exception exception){
//        Map<String, Object> responseData = new HashMap<>();
//        if(exception instanceof BusinessException){
//            BusinessException businessException = (BusinessException)exception;
//            responseData.put("errCode", businessException.getErrorCode());
//            responseData.put("errMessage",businessException.getErrorMessage());
//        }else{
//            responseData.put("errCode", BusinessError.UNKNOWN_ERROR.getErrorCode());
//            responseData.put("errMessage",BusinessError.UNKNOWN_ERROR.getErrorMessage());
//        }
//        return CommonReturnType.create(responseData, "fail");
//    }
}
