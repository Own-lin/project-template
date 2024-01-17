package com.ader1y.template.model.base;

import lombok.Data;

@Data
public class R<T> {


    private Integer code;

    private T data;

    /**
     * 业务异常码
     */
    private String businessCode;

    public R(){}

    private R(T data){
        this.code = 200;
        this.data = data;
    }

    private R(Integer code, String bizCode){
        this.code = code;
        this.businessCode = bizCode;
    }

    public static <T> R<T> success(T data){
        return new R<>(data);
    }

    public static <T> R<T> fail(Integer code, String businessCode){
        return new R<>(code, businessCode);
    }

    public static <T> R<T> fail(BaseCode baseCode){
        return new R<>(baseCode.getCode(), baseCode.getBizCode());
    }

}
