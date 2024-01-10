package com.ader1y.template.core.support.ability;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class Monitor {

    private static final Logger LOG = getLogger(Monitor.class);



    public void sendNotify(String message){
        LOG.warn(message);
    }

}
