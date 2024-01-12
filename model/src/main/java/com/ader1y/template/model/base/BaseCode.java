package com.ader1y.template.model.base;

public interface BaseCode {


    int getCode();

    String getBizCode();

    void throwEx();

    void throwEx(Object... args);

}
