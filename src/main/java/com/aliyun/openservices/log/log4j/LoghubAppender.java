package com.aliyun.openservices.log.log4j;

import com.aliyun.openservices.aliyun.log.producer.LogProducer;
import com.aliyun.openservices.aliyun.log.producer.Producer;
import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfigs;
import com.aliyun.openservices.aliyun.log.producer.errors.ProducerException;
import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.aliyun.openservices.log.common.LogItem;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LoghubAppender extends AppenderSkeleton {

  private String project;

  private String endpoint;

  private String accessKeyId;

  private String accessKeySecret;

  private String userAgent = "log4j";

  private String logStore;

  private ProducerConfig producerConfig = new ProducerConfig(new ProjectConfigs());

  private ProjectConfig projectConfig;

  private String topic = "";

  private String source = "";

  private String timeFormat = "yyyy-MM-dd'T'HH:mm:ssZ";

  private String timeZone = "UTC";

  private Producer producer;

  private DateTimeFormatter formatter;

  @Override
  public void activateOptions() {
    super.activateOptions();
    formatter = DateTimeFormat.forPattern(timeFormat).withZone(DateTimeZone.forID(timeZone));
    producer = createProducer();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          doClose();
        } catch (Exception e) {
          LogLog.error("Failed to close LoghubAppender.", e);
        }
      }
    });
  }

  public void close() {
    try {
      doClose();
    } catch (Exception e) {
      LogLog.error("Failed to close LoghubAppender.", e);
    }
  }

  private void doClose() throws InterruptedException, ProducerException {
    producer.close();
  }

  public boolean requiresLayout() {
    return true;
  }

  public Producer createProducer() {
    projectConfig = buildProjectConfig();
    producerConfig.getProjectConfigs().put(projectConfig);
    return new LogProducer(producerConfig);
  }

  private ProjectConfig buildProjectConfig() {
    return new ProjectConfig(project, endpoint, accessKeyId, accessKeySecret, null, userAgent);
  }

  @Override
  protected void append(LoggingEvent event) {
    LogItem logItem = new LogItem();
    logItem.SetTime((int) (event.getTimeStamp() / 1000));
    DateTime dateTime = new DateTime(event.getTimeStamp());
    logItem.PushBack("time", dateTime.toString(formatter));
    logItem.PushBack("level", event.getLevel().toString());
    logItem.PushBack("thread", event.getThreadName());
    logItem.PushBack("location", event.getLocationInformation().fullInfo);
    logItem.PushBack("message", String.valueOf(event.getMessage()));

    String throwable = getThrowableStr(event);
    if (throwable != null) {
      logItem.PushBack("throwable", throwable);
    }

    if (getLayout() != null) {
      logItem.PushBack("log", getLayout().format(event));
    }

    Map properties = event.getProperties();
    if (properties.size() > 0) {
      Object[] keys = properties.keySet().toArray();
      Arrays.sort(keys);
      for (int i = 0; i < keys.length; i++) {
        logItem.PushBack(keys[i].toString(), properties.get(keys[i])
            .toString());
      }
    }
    try {
      producer.send(projectConfig.getProject(), logStore, topic, source, logItem,
          new LoghubAppenderCallback(projectConfig.getProject(), logStore, topic, source, logItem));
    } catch (Exception e) {
      LogLog.error(
          "Failed to send log, project=" + project
              + ", logStore=" + logStore
              + ", topic=" + topic
              + ", source=" + source
              + ", logItem=" + logItem, e);
    }
  }

  private String getThrowableStr(LoggingEvent event) {
    ThrowableInformation throwable = event.getThrowableInformation();
    if (throwable == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    for (String s : throwable.getThrowableStrRep()) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(System.getProperty("line.separator"));
      }
      sb.append(s);
    }
    return sb.toString();
  }

  public String getProject() {
    return project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = accessKeyId;
  }

  public String getAccessKeySecret() {
    return accessKeySecret;
  }

  public void setAccessKeySecret(String accessKeySecret) {
    this.accessKeySecret = accessKeySecret;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getLogStore() {
    return logStore;
  }

  public void setLogStore(String logStore) {
    this.logStore = logStore;
  }

  public int getTotalSizeInBytes() {
    return producerConfig.getTotalSizeInBytes();
  }

  public void setTotalSizeInBytes(int totalSizeInBytes) {
    producerConfig.setTotalSizeInBytes(totalSizeInBytes);
  }

  public long getMaxBlockMs() {
    return producerConfig.getMaxBlockMs();
  }

  public void setMaxBlockMs(long maxBlockMs) {
    producerConfig.setMaxBlockMs(maxBlockMs);
  }

  public int getIoThreadCount() {
    return producerConfig.getIoThreadCount();
  }

  public void setIoThreadCount(int ioThreadCount) {
    producerConfig.setIoThreadCount(ioThreadCount);
  }

  public int getBatchSizeThresholdInBytes() {
    return producerConfig.getBatchSizeThresholdInBytes();
  }

  public void setBatchSizeThresholdInBytes(int batchSizeThresholdInBytes) {
    producerConfig.setBatchSizeThresholdInBytes(batchSizeThresholdInBytes);
  }

  public int getBatchCountThreshold() {
    return producerConfig.getBatchCountThreshold();
  }

  public void setBatchCountThreshold(int batchCountThreshold) {
    producerConfig.setBatchCountThreshold(batchCountThreshold);
  }

  public int getLingerMs() {
    return producerConfig.getLingerMs();
  }

  public void setLingerMs(int lingerMs) {
    producerConfig.setLingerMs(lingerMs);
  }

  public int getRetries() {
    return producerConfig.getRetries();
  }

  public void setRetries(int retries) {
    producerConfig.setRetries(retries);
  }

  public int getMaxReservedAttempts() {
    return producerConfig.getMaxReservedAttempts();
  }

  public void setMaxReservedAttempts(int maxReservedAttempts) {
    producerConfig.setMaxReservedAttempts(maxReservedAttempts);
  }

  public long getBaseRetryBackoffMs() {
    return producerConfig.getBaseRetryBackoffMs();
  }

  public void setBaseRetryBackoffMs(long baseRetryBackoffMs) {
    producerConfig.setBaseRetryBackoffMs(baseRetryBackoffMs);
  }

  public long getMaxRetryBackoffMs() {
    return producerConfig.getMaxRetryBackoffMs();
  }

  public void setMaxRetryBackoffMs(long maxRetryBackoffMs) {
    producerConfig.setMaxRetryBackoffMs(maxRetryBackoffMs);
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTimeFormat() {
    return timeFormat;
  }

  public void setTimeFormat(String timeFormat) {
    this.timeFormat = timeFormat;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }
}
