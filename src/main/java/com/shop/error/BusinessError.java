package com.shop.error;

public enum BusinessError implements CommonError {
    //通用错误类型
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),

    //未知错误
    UNKNOWN_ERROR(10002, "未知错误"),

    //20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_LOGIN_FAIL(20002, "用户手机号或密码不正确"),
    USER_NOT_LOGIN(20003,"用户未登录"),
    //30000开头为交易信息错误
    STOCK_NOT_ENOUTH(30001,"库存不足")
    ;

    private int errorCode;
    private String errMessage;

    BusinessError(int errorCode, String errMessage) {
        this.errMessage = errMessage;
        this.errorCode = errorCode;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errMessage;
    }

    @Override
    public CommonError setErrorMessage(String errorMessage) {
        this.errMessage = errorMessage;
        return this;
    }
}
