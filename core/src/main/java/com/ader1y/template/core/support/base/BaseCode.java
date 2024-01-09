package com.ader1y.template.core.support.base;

public interface BaseCode {


    int getCode();

    String getBizCode();

    void throwEx();

    void throwEx(Object... args);

}
