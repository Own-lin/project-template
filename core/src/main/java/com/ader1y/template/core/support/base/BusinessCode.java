package com.ader1y.template.core.support.base;


import lombok.Getter;

import java.text.MessageFormat;

@Getter
public enum BusinessCode implements BaseCode{



    ;

    private final int code;

    private final String bizCode;

    BusinessCode(int code, String bizCode) {
        this.code = code;
        this.bizCode = bizCode;
    }

    @Override
    public void throwEx() {
        throw new BusinessException(this);
    }

    @Override
    public void throwEx(Object... args) {
        throw new BusinessException(this, MessageFormat.format(this.getBizCode(), args));
    }

}
