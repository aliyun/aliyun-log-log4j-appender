package com.aliyun.openservices.log.log4j;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by brucewu on 2018/1/5.
 */
public class LoghubAppenderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoghubAppenderTest.class);

    @Test
    public void testLogCommonMessage() {
        LOGGER.warn("This is a test common message logged by log4j.");
    }

    @Test
    public void testLogThrowable() {
        LOGGER.error("This is a test error message logged by log4j.",
                new UnsupportedOperationException("Log4j UnsupportedOperationException"));
    }

    @Test
    public void testLogLevelInfo() {
        LOGGER.info("This is a test error message logged by log4j, level is info, should not be logged.");
    }
}
