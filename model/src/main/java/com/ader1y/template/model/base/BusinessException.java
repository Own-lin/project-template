package com.ader1y.template.model.base;

public class BusinessException extends BaseException{

    public BusinessException(BaseCode baseCode, Throwable cause) {
        super(baseCode, cause);
    }

    public BusinessException(BaseCode baseCode, String message, Throwable cause) {
        super(baseCode, message, cause);
    }

}
