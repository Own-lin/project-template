package com.ader1y.template.model.base;

public class BaseException extends RuntimeException{

    private BaseCode baseCode;

    public BaseException(BaseCode baseCode) {
        super(baseCode.getBizCode());
        this.baseCode = baseCode;
    }

    public BaseException(BaseCode baseCode, String bizCode) {
        super(bizCode);
        this.baseCode = baseCode;
    }

    public BaseCode getCodeEnum(){
        return baseCode;
    }

}
