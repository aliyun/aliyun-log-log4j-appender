package com.aliyun.openservices.log.log4j;

import com.aliyun.openservices.aliyun.log.producer.Callback;
import com.aliyun.openservices.aliyun.log.producer.Result;
import com.aliyun.openservices.log.common.LogItem;
import org.apache.log4j.helpers.LogLog;

/**
 * Created by brucewu on 2018/1/6.
 */
public class LoghubAppenderCallback implements Callback {

  private String project;

  private String logStore;

  private String topic;

  private String source;

  private LogItem logItem;

  public LoghubAppenderCallback(String project, String logStore, String topic, String source,
      LogItem logItem) {
    super();
    this.project = project;
    this.logStore = logStore;
    this.topic = topic;
    this.source = source;
    this.logItem = logItem;
  }

  @Override
  public void onCompletion(Result result) {
    if (!result.isSuccessful()) {
      LogLog.error(
          "Failed to send log, project=" + project
              + ", logStore=" + logStore
              + ", topic=" + topic
              + ", source=" + source
              + ", logItem=" + logItem
              + ", errorCode=" + result.getErrorCode()
              + ", errorMessage=" + result.getErrorMessage());
    }
  }
}
