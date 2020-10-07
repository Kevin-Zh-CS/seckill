package com.shop.error;

public interface CommonError {
    int getErrorCode();
    String getErrorMessage();
    CommonError setErrorMessage(String errorMessage);
}
