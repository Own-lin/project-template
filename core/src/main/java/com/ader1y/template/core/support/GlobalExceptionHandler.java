package com.ader1y.template.core.support;


import com.ader1y.template.core.support.event.WarningEvent;
import com.ader1y.template.model.base.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R<String> handle(Exception e) {
        String stackTrace = getStackTrace(e);
        warningLog(e, stackTrace);
        sendWarningEvent(ExceptionLevel.HIGHEST, stackTrace);
        return R.fail(500, "哎呀 出错了～ 请等会再试吧❀");
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public R<String> handle(BusinessException e) {
        infoLog(e);
        return R.fail(e.getCodeEnum());
    }

    @ResponseBody
    @ExceptionHandler(value = BadRequestException.class)
    public R<String> handle(BadRequestException e) {
        String stackTrace = getStackTrace(e);
        warningLog(e, stackTrace);
        sendWarningEvent(ExceptionLevel.BUSINESS, stackTrace);
        return R.fail(e.getCodeEnum());
    }

    /**
     * 对JSR-303(@Valid @Validation等)规则中的异常进行捕获
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<String> handlerEx(HttpServletRequest request, MethodArgumentNotValidException e){
        BusinessCode code = BusinessCode.UN_SUPPORT_PARAM;
        String source = e.getAllErrors().get(0).getDefaultMessage();
        LOG.info(code.formatBizCode(source));

        return R.fail(code.getCode(), source);
    }

    private static String getStackTrace(final Throwable e){
        String stackTrace = ExceptionUtils.getStackTrace(e);
        String[] stackTraceArray = StringUtils.split(stackTrace, "\n", 7);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 6; i++){
            sb.append(stackTraceArray[i]).append("\n");
        }

        return sb.toString();
    }

    private static final String LOG_MESSAGE = "\n Exception message: {};\n StackTrace: {}";

    private static void infoLog(Exception e){
        LOG.info(e.getMessage());
    }

    private static void warningLog(Exception e, String stackTrace){
        LOG.warn(LOG_MESSAGE, e.getMessage(), stackTrace);
    }

    private void sendWarningEvent(ExceptionLevel exLevel, String stackTrace){
        eventPublisher.publishEvent(new WarningEvent(exLevel, stackTrace));
    }

}
