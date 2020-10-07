package com.shop.error;
//包装器业务异常实现
public class BusinessException extends Exception implements CommonError{

    private CommonError commonError;

    //直接接受BussionError传参，构造业务异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    //接收自定义的errMessage的方法构造业务异常
    public BusinessException(CommonError commonError, String errMessage){
        super();
        this.commonError = commonError;
        commonError.setErrorMessage(errMessage);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMessage() {
        return this.commonError.getErrorMessage();
    }

    @Override
    public CommonError setErrorMessage(String errorMessage) {
        this.commonError.setErrorMessage(errorMessage);
        return this;
    }
}
