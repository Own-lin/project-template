package com.ader1y.template.core.support;


import com.ader1y.template.core.support.event.WarningEvent;
import com.ader1y.template.model.base.*;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Resource
    private ApplicationEventPublisher eventPublisher;

    public void scheduleExHandle(Throwable e) {
        String stackTrace = getStackTrace(e);
        warningLog(e, stackTrace);
        sendWarningEvent(ExceptionLevel.HIGH, e, stackTrace);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R<String> handle(Exception e) {
        String stackTrace = getStackTrace(e);
        warningLog(e, stackTrace);
        sendWarningEvent(ExceptionLevel.HIGHEST, e, stackTrace);
        return R.fail(500, "哎呀 出错了～ 请等会再试吧❀");
    }

    @ResponseBody
    @ExceptionHandler(NoResourceFoundException.class)
    public R<String> handle(NoResourceFoundException e) {
        LOG.warn("未找到资源: {}", e.getMessage());
        return R.fail(404, "未找到对应资源");
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public R<String> handle(BusinessException e) {
        //  业务异常为可预期的, 只需要记录info日志
        infoLog(e);
        return R.fail(e.getCodeEnum(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = BadRequestException.class)
    public R<String> scheduleExHandle(BadRequestException e) {
        String stackTrace = getStackTrace(e);
        warningLog(e, stackTrace);
        sendWarningEvent(ExceptionLevel.BUSINESS, e, stackTrace);
        return R.fail(e.getCodeEnum(), e.getMessage());
    }

    /**
     * 对JSR-303(@Valid @Validation等)规则中的异常进行捕获
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<String> handlerEx(MethodArgumentNotValidException e){
        //  可预期异常, 只需要记录info日志
        BusinessCode code = BusinessCode.UN_SUPPORT_PARAM;
        String source = e.getAllErrors().get(0).getDefaultMessage();
        LOG.info(code.formatBizCode(source));

        return R.fail(code.getCode(), source);
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

    private static final String LOG_MESSAGE = "\n Exception message: {};\n StackTrace: {}";

    private static void infoLog(Exception e){
        LOG.info(e.getMessage());
    }

    private static void warningLog(Throwable e, String stackTrace){
        LOG.warn(LOG_MESSAGE, e.getMessage(), stackTrace);
    }

    private void sendWarningEvent(ExceptionLevel exLevel, Throwable t, String stackTrace){
        eventPublisher.publishEvent(new WarningEvent(exLevel, t.getMessage(), stackTrace));
    }

}
