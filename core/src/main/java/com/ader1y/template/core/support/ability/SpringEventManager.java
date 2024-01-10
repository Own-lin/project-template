package com.ader1y.template.core.support.ability;

import com.ader1y.template.core.support.event.WarningEvent;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SpringEventManager {

    private static final Logger LOG = getLogger(SpringEventManager.class);

    @Resource
    private Monitor monitor;

    @EventListener(WarningEvent.class)
    public void sendWarningNotify(WarningEvent event) {
        monitor.sendNotify(event.print());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        LOG.info("\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80 应用启动成功.");
    }

}
