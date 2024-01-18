package com.ader1y.template.core.config;

import com.ader1y.template.core.support.GlobalExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduleAsyncPool());
    }

    @Bean
    public ThreadPoolExecutor scheduleAsyncPool() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        threadFactoryExceptionHandlerDecorator(executor, "schedule-pool-%d");
        return executor;
    }

    private void threadFactoryExceptionHandlerDecorator(ThreadPoolExecutor org, String nameFormat) {
        org.setThreadFactory(new ThreadFactoryBuilder()
                .setNameFormat(nameFormat)
                .setUncaughtExceptionHandler(new ScheduleExDecorator())
                .build());
    }

    /**
     * 异常装饰对象
     */
    class ScheduleExDecorator implements ErrorHandler, Thread.UncaughtExceptionHandler {


        @Resource
        private GlobalExceptionHandler exceptionHandler;

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            exceptionHandler.scheduleExHandle(e);
        }

        @Override
        public void handleError(Throwable t) {
            exceptionHandler.scheduleExHandle(t);
        }

    }


}
