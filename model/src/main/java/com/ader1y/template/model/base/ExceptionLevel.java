package com.ader1y.template.model.base;

import lombok.Getter;

@Getter
public enum ExceptionLevel {

    HIGHEST(0, "非常紧急!"),

    HIGH(1, "紧急!"),

    MEDIUM(2, "中等."),

    LOW(3, "低"),

    BUSINESS(4, "业务异常")


    ;

    private final int level;

    private final String title;

    ExceptionLevel(int level, String title) {
        this.level = level;
        this.title = title;
    }
}
