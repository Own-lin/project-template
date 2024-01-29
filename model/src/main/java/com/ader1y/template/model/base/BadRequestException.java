package com.ader1y.template.model.base;

public class BadRequestException extends BaseException{

    public BadRequestException(BaseCode baseCode, Throwable cause) {
        super(baseCode, cause);
    }

    public BadRequestException(BaseCode baseCode, String message, Throwable cause) {
        super(baseCode, message, cause);
    }
}
