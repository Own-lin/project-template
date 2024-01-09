package com.ader1y.template.core.support.base;

public class BadRequestException extends BaseException{

    public BadRequestException(BaseCode baseCode) {
        super(baseCode);
    }

    public BadRequestException(BaseCode baseCode, String bizCode) {
        super(baseCode, bizCode);
    }

}
