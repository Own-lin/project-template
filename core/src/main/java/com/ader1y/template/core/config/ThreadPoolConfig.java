package com.ader1y.template.core.config;

import com.ader1y.template.core.support.GlobalExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig implements SchedulingConfigurer {

    @Resource
    private GlobalExceptionHandler exceptionHandler;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduleAsyncPool());
    }

    @Bean
    public TaskExecutor scheduleAsyncPool() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                new ThreadFactoryBuilder()
                        .setNameFormat("schedule-pool-%d")
                        .build(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        return decoratorThreadPool(executor);
    }

    private TaskExecutor decoratorThreadPool(ScheduledThreadPoolExecutor org){
        ConcurrentTaskScheduler exec = new ConcurrentTaskScheduler(org);
        exec.setErrorHandler(new ScheduleExDecorator());
        return exec;
    }

    private ThreadPoolExecutor decoratorThreadFactoryExceptionHandler(ThreadPoolExecutor org){
        if (org.getThreadFactory() == null){
            org.setThreadFactory(new ThreadFactoryBuilder()
                    .setUncaughtExceptionHandler(new ScheduleExDecorator())
                    .build());
        }

        return org;
    }

    class ScheduleExDecorator implements ErrorHandler, Thread.UncaughtExceptionHandler {

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
