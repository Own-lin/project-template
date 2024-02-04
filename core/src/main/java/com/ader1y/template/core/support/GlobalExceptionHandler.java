package com.ader1y.template.core.support;


import com.ader1y.template.core.support.event.WarningEvent;
import com.ader1y.template.model.base.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
        WarningEvent event = WarningEvent.buildSystemWarning(ExceptionLevel.HIGH, e);
        warningLog(event.print());
        sendWarningEvent(event);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R<String> handle(HttpServletResponse response, Exception e) {
        WarningEvent event = WarningEvent.buildSystemWarning(ExceptionLevel.HIGHEST, e);
        warningLog(event.detailPrint());
        sendWarningEvent(event);
        setContentType(response);
        return R.fail(500, "哎呀 出错了～ 请等会再试吧❀");
    }

    @ResponseBody
    @ExceptionHandler(NoResourceFoundException.class)
    public R<String> handle(HttpServletResponse response, NoResourceFoundException e) {
        LOG.warn("未找到资源: {}", e.getMessage());
        setContentType(response);
        return R.fail(404, "未找到对应资源");
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public R<String> handle(HttpServletResponse response, BusinessException e) {
        //  业务异常为可预期的, 只需要记录info日志
        infoLog(e);
        setContentType(response);
        return R.fail(e.getCodeEnum(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = BadRequestException.class)
    public R<String> scheduleExHandle(HttpServletResponse response, BadRequestException e) {
        WarningEvent event = WarningEvent.buildBusinessWarning(ExceptionLevel.BUSINESS, e, e.getMessage());
        warningLog(event.detailPrint());
        sendWarningEvent(event);
        setContentType(response);
        return R.fail(e.getCodeEnum(), e.getMessage());
    }

    /**
     * 对JSR-303(@Valid @Validation等)规则中的异常进行捕获
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<String> handlerEx(HttpServletResponse response, MethodArgumentNotValidException e){
        //  可预期异常, 只需要记录info日志
        BusinessCode code = BusinessCode.UN_SUPPORT_PARAM;
        String source = e.getAllErrors().get(0).getDefaultMessage();
        LOG.info(code.formatBizCode(source));

        setContentType(response);
        return R.fail(code.getCode(), source);
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<String> handlerEx(HttpServletResponse response, MissingServletRequestParameterException e){
        BadCode code = BadCode.UN_SUPPORT_REQUEST_URL;
        //  只记录日志, 不需要发送通知
        WarningEvent event = WarningEvent.buildBusinessWarning(ExceptionLevel.MEDIUM, e, code.getBizCode());
        warningLog(event.detailPrint());
        sendWarningEvent(event);
        setContentType(response);
        return R.fail(code);
    }

    private static void infoLog(Exception e){
        LOG.info(e.getMessage());
    }

    private static void warningLog(String errorMsg){
        LOG.warn(errorMsg);
    }

    private void sendWarningEvent(WarningEvent warningEvent){
        eventPublisher.publishEvent(warningEvent);
    }

    private static void setContentType(HttpServletResponse response){
        response.setContentType("application/json");
    }

}
