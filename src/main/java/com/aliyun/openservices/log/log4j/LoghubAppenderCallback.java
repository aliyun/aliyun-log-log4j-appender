package com.aliyun.openservices.log.log4j;

import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.producer.ILogCallback;
import com.aliyun.openservices.log.response.PutLogsResponse;
import org.apache.log4j.helpers.LogLog;

import java.util.List;

/**
 * Created by brucewu on 2018/1/6.
 */
public class LoghubAppenderCallback extends ILogCallback {

    private String project;

    private String logstore;

    private String topic;

    private String source;

    private List<LogItem> logItems;

    public LoghubAppenderCallback(String project, String logstore, String topic, String source, List<LogItem> logItems) {
        super();
        this.project = project;
        this.logstore = logstore;
        this.topic = topic;
        this.source = source;
        this.logItems = logItems;
    }

    @Override
    public void onCompletion(PutLogsResponse response, LogException e) {
        if (e != null) {
            LogLog.error("Failed to putLogs. project=" + project + " logstore=" + logstore + " topic=" + topic +
                    " source=" + source + " logItems=" + logItems, e);
        }
    }
}
