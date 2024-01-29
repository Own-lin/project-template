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
     * 系统异常事件, 直接处理原始异常
     */
    public WarningEvent(ExceptionLevel level, Throwable e) {
        this.level = level;
        this.sourceMessage = e.getMessage();
        this.stackTrace = getStackTrace(e);
    }
    /**
     *   业务异常事件, 需要获取业务实际异常 即上一层异常. <br></br>
     * 需要指定bizMessage(业务抽象异常中的{@link BaseCode})
     */

    public WarningEvent(ExceptionLevel level, Throwable e, String bizMessage) {
        this.level = level;
        this.sourceMessage = e.getCause().getMessage();
        this.bizMessage = bizMessage;
        this.stackTrace = getStackTrace(e.getCause());
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

    private static String getStackTrace(final Throwable e){
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stacks = e.getStackTrace();
        for (int i = 0; i < 6; i++){
            sb.append(stacks[i].toString()).append("\n");
        }
        sb.append("......");

        return sb.toString();
    }

}
