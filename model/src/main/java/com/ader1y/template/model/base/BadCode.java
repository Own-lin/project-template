package com.ader1y.template.model.base;

import lombok.Getter;

import java.text.MessageFormat;

/**
 * 糟糕(恶意)请求的业务异常码.
 */
@Getter
public enum BadCode implements BaseCode{

    UN_EXPECTED(2001, "请不要使用非预期的操作"),

    LENGTH_LIMIT(2002, "长度不在限制内"),

    EXPORT_ERROR(2003, "导出暂不可用, 请稍后再试"),

    UN_SUPPORT_REQUEST_URL(2004, "不支持的请求路径")

    ;

    private final int code;

    private final String bizCode;

    BadCode(int code, String bizCode) {
        this.code = code;
        this.bizCode = bizCode;
    }

    @Override
    public void throwEx(Exception source) {
        throw new BadRequestException(this, source);
    }

    @Override
    public void throwEx(Exception source, Object... infoArgs) {
        String message = this.getBizCode();
        try{
            message = MessageFormat.format(this.getBizCode(), infoArgs);
        }catch (IllegalArgumentException ex){
            BadCode.UN_EXPECTED.throwEx(source);
        }
        throw new BadRequestException(this, message, source);
    }
}
