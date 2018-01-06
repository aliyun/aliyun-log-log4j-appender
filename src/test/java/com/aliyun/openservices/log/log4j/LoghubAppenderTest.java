package com.aliyun.openservices.log.log4j;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by brucewu on 2018/1/5.
 */
public class LoghubAppenderTest {

    private static Logger logger = LogManager.getLogger(LoghubAppenderTest.class);

    @Test
    public void testLogCommonMessage() {
        logger.warn("This is a test common message logged by log4j.");
    }

    @Test
    public void testLogThrowable() {
        logger.error("This is a test error message logged by log4j.",
                new UnsupportedOperationException("log4j appender test."));
    }

    @Test
    public void testLogLevelInfo() {
        logger.info("This is a test error message logged by log4j, level is info,should not be logged.");
    }

}
