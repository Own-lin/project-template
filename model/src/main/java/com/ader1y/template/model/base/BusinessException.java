package com.ader1y.template.model.base;

public class BusinessException extends BaseException{


    public BusinessException(BaseCode baseCode) {
        super(baseCode);
    }

    public BusinessException(BaseCode baseCode, String bizCode) {
        super(baseCode, bizCode);
    }

}
