package com.ader1y.template.core.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Get SpringBean on applicationContext, cannot use this util before {@link AbstractApplicationContext#finishRefresh()}
 *
 * @see ApplicationContextAware
 * @see ApplicationContext
 */
@Component("springBeanUtil")
public class SpringBeanUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext ;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        if (applicationContext == null) {
            applicationContext = arg0;
        }
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
