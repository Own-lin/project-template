package com.ader1y.template.core.config;

import com.ader1y.template.core.support.GlobalExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Resource
    private GlobalExceptionHandler exceptionHandler;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduleAsyncPool());
    }

    @Bean
    public ConcurrentTaskScheduler scheduleAsyncPool() {
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                new ThreadFactoryBuilder()
                        .setNameFormat("schedule-pool-%d")
                        .build(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        ConcurrentTaskScheduler exec = new ConcurrentTaskScheduler(executor);
        exec.setErrorHandler(new ScheduleExDecorator());
        return exec;
    }

    class ScheduleExDecorator implements ErrorHandler {

        @Override
        public void handleError(Throwable t) {
            exceptionHandler.scheduleExHandle(t);
        }

    }


}
