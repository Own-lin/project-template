package com.ader1y.template.model.base;

import java.text.MessageFormat;

public interface BaseCode {


    int getCode();

    String getBizCode();

    void throwEx();

    void throwEx(Object... args);

    default String formatBizCode(Object... args) {
        return MessageFormat.format(getBizCode(), args);
    }

}
