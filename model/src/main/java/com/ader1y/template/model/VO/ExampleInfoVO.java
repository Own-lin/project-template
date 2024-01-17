package com.ader1y.template.model.VO;

import lombok.Data;

@Data
public class ExampleInfoVO {

    private String content;

    private String remark;

    public ExampleInfoVO(){}

    public ExampleInfoVO(String content, String remark) {
        this.content = content;
        this.remark = remark;
    }
}
