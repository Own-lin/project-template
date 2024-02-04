package com.ader1y.template.core.support.event;


import com.ader1y.template.model.base.BaseCode;
import com.ader1y.template.model.base.ExceptionLevel;
import lombok.Data;

@Data
public class WarningEvent {

    private ExceptionLevel level;

    /**
     * 异常的原始信息, 根据规范 应该为Exception#getCause#getMessage的内容
     */
    private String sourceMessage;

    /**
     * 异常的业务信息, 为空说明不是业务异常, 应该为Exception#getMessage的内容
     */
    private String bizMessage;

    /**
     * 异常堆栈, 应该为Exception#getCause中的堆栈信息
     */
    private String stackTrace;

    /**
     * 更详细的异常堆栈
     */
    private String moreStackTrace;

    /**
     * 系统异常事件
     */
    public static WarningEvent buildSystemWarning(ExceptionLevel level, Throwable e){
        return new WarningEvent(level, e);
    }

    /**
     * 业务异常事件
     */
    public static WarningEvent buildBusinessWarning(ExceptionLevel level, Throwable e, String bizMessage){
        return new WarningEvent(level, e, bizMessage);
    }

    public WarningEvent() {}

    /**
     * 系统异常事件, 直接处理原始异常
     */
    private WarningEvent(ExceptionLevel level, Throwable e) {
        this.level = level;
        this.sourceMessage = e.getMessage();
        StackTraceElement[] stacks = e.getStackTrace();
        this.stackTrace = getStackTrace(stacks, 6);
        this.moreStackTrace = getStackTrace(stacks, 12);
    }

    /**
     *   业务异常事件, 需要获取业务实际异常 即上一层异常. <br></br>
     * 需要指定bizMessage(业务抽象异常中的{@link BaseCode})
     */
    private WarningEvent(ExceptionLevel level, Throwable e, String bizMessage) {
        this.level = level;
        this.sourceMessage = e.getCause().getMessage();
        this.bizMessage = bizMessage;
        StackTraceElement[] stacks = e.getCause().getStackTrace();
        this.stackTrace = getStackTrace(stacks, 6);
        this.moreStackTrace = getStackTrace(stacks, 12);
    }

    public String print() {

        return "异常等级: " + level.getTitle() +
                "\n" +
                "异常信息: " + sourceMessage +
                "\n" +
                "业务信息: " + bizMessage +
                "\n" +
                "异常堆栈: " + stackTrace;
    }

    public String detailPrint() {

        return "异常等级: " + level.getTitle() +
                "\n" +
                "异常信息: " + sourceMessage +
                "\n" +
                "业务信息: " + bizMessage +
                "\n" +
                "异常堆栈: " + moreStackTrace;
    }

    private static String getStackTrace(StackTraceElement[] stacks, int deep){
        if (deep < 0) deep = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < deep; i++){
            sb.append(stacks[i].toString()).append("\n");
        }
        sb.append("......");

        return sb.toString();
    }

}
