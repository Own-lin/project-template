package com.ader1y.template.core.support.event;


import com.ader1y.template.model.base.ExceptionLevel;
import lombok.Data;

@Data
public class WarningEvent {

    private ExceptionLevel level;

    private String sourceMessage;

    private String stackTrace;

    public WarningEvent(ExceptionLevel level, String sourceMessage, String stackTrace) {
        this.level = level;
        this.stackTrace = stackTrace;
    }

    public String print() {

        return "异常等级: " + level.getTitle() +
                "\n" +
                "异常消息: " + sourceMessage +
                "\n" +
                "异常堆栈: " + stackTrace;
    }

}
