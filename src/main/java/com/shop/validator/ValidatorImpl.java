package com.shop.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {
    private Validator validator;

    //实现校验方法，并返回校验结果
    public ValidationResult validate(Object bean){
        ValidationResult validationResult = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if(constraintViolationSet.size() > 0){
            validationResult.setHasErrors(true);
            for (ConstraintViolation<Object> violation : constraintViolationSet) {
                String errMessage = violation.getMessage();
                String propertyName = violation.getPropertyPath().toString();
                validationResult.getErrorMsgMap().put(propertyName, errMessage);
            }
        }
        return validationResult;
    }

    @Override
    public void afterPropertiesSet() {
        //工厂模式实例化
        //TODO:需理解
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
