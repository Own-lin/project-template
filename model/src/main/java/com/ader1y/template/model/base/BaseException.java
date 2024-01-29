package com.ader1y.template.model.base;

/**
 * 所有业务异常的父类, 上层异常需要通过{@link Throwable}参数进行传递
 */
public class BaseException extends RuntimeException{

    private BaseCode baseCode;

    public BaseException(BaseCode baseCode, Throwable cause) {
        super(baseCode.getBizCode(), cause);
        this.baseCode = baseCode;
    }

    public BaseException(BaseCode baseCode, String message, Throwable cause) {
        super(message, cause);
        this.baseCode = baseCode;
    }

    public BaseCode getCodeEnum(){
        return baseCode;
    }

}
