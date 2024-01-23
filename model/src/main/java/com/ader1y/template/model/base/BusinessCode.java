package com.ader1y.template.model.base;


import lombok.Getter;

import java.text.MessageFormat;

/**
 * 业务异常错误码的枚举以及异常处理(该类下所有异常都应该为1xxx码).</p>
 * 可通过传入参数来自定义异常信息.
 */
@Getter
public enum BusinessCode implements BaseCode{

    /**
     * 重复数据
     */
    DUPLICATE(1001, "已存在相同{0}"),

    /**
     * 请求参数错误
     */
    UN_SUPPORT_PARAM(1002, "{0}"),

    REQUEST_LIMIT(1003, "操作过快, 请不要频繁点击哦~")


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
        String message = this.getBizCode();
        try{
            message = MessageFormat.format(this.getBizCode(), args);
        }catch (IllegalArgumentException ex){
            BadCode.UN_EXPECTED.throwEx();
        }
        throw new BusinessException(this, message);
    }

}
