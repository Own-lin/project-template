package com.ader1y.template.core.support.base;

import lombok.Getter;

import java.text.MessageFormat;

/**
 * 糟糕(恶意)请求的业务异常码.
 */
@Getter
public enum BadCode implements BaseCode{

    UN_EXPECTED(2001, "请不要使用非预期的操作"),

    LENGTH_LIMIT(2002, "长度不在限制内")

    ;

    private final int code;

    private final String bizCode;

    BadCode(int code, String bizCode) {
        this.code = code;
        this.bizCode = bizCode;
    }

    @Override
    public void throwEx() {
        throw new BadRequestException(this);
    }

    @Override
    public void throwEx(Object... args) {
        String message = this.getBizCode();
        try{
            message = MessageFormat.format(this.getBizCode(), args);
        }catch (IllegalArgumentException ex){
            BadCode.UN_EXPECTED.throwEx();
        }
        throw new BadRequestException(this, message);
    }
}
