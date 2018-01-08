package com.aliyun.openservices.log.log4j.example;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by brucewu on 2018/1/8.
 */
public class Log4jAppenderExample {

    private static final Logger LOGGER = LogManager.getLogger(Log4jAppenderExample.class);

    public static void main(String[] args) {
        LOGGER.trace("log4j trace log");
        LOGGER.debug("log4j debug log");
        LOGGER.info("log4j info log");
        LOGGER.warn("log4j warn log");
        LOGGER.error("log4j error log", new RuntimeException("Runtime Exception"));
    }

}
