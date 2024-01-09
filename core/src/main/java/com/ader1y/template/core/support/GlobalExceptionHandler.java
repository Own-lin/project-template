package com.ader1y.template.core.support;


import com.ader1y.template.core.support.base.BadRequestException;
import com.ader1y.template.core.support.base.BusinessException;
import com.ader1y.template.core.support.base.R;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R<String> handle(Exception e) {
        e.printStackTrace();
        return R.fail(500, "哎呀 出错了～ 请等会再试吧❀");
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public R<String> handle(BusinessException e) {
        warning(e);
        return R.fail(e.getCodeEnum());
    }

    @ResponseBody
    @ExceptionHandler(value = BadRequestException.class)
    public R<String> handle(BadRequestException e) {
        warning(e);
        return R.fail(e.getCodeEnum());
    }

    private static final String LOG_MESSAGE = "\n Exception message: {};\n StackTrace: {}";

    private static void warning(Exception e){
        LOG.warn(LOG_MESSAGE, e.getMessage(), ExceptionUtils.getStackTrace(e));
    }

}
