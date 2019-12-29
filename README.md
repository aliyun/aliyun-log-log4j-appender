# Log4j Appender

[![Build Status](https://travis-ci.org/aliyun/aliyun-log-log4j-appender.svg?branch=master)](https://travis-ci.org/aliyun/aliyun-log-log4j-appender)
[![License](https://img.shields.io/badge/license-Apache2.0-blue.svg)](/LICENSE)

[中文版README](/README_CN.md)

## Aliyun Log Log4j Appender

Apache log4j is an Apache Software Foundation Project. You can control the destination of the log through Log4j. It can be console, file, GUI components, socket, NT event log, syslog. You can control the output format for each log as well. You can control the generation process of the log through log level. The most interesting thing is you can complete the above things through a configuration file and without any code modification.

You can set the destination of your log to AliCloud Log Service through `Aliyun Log Log4j Appender`. The format of the log in AliCloud Log Service is as follows:
```
level: ERROR
location: com.aliyun.openservices.log.log4j.example.Log4jAppenderExample.main(Log4jAppenderExample.java:16)
message: error log
throwable: java.lang.RuntimeException: xxx
thread: main
time: 2018-01-02T03:15+0000
log: 0 [main] ERROR com.aliyun.openservices.log.log4j.example.Log4jAppenderExample - error log
__source__: xxx
__topic__: yyy
```
Field Specifications:
+ `level` stands for log level
+ `location` is logs's output position
+ `message` is the content of the log
+ `throwable` is exception of the log (this field will appear only if the exception is recorded)
+ `thread` stands for thread name
+ `time` is the log's generation time (you can configure it's format through timeFormat and timeZone)
+ `log` is custom log format
+ `__source__` is the log's source, you can specify its value in conf file
+ `__topic__` is the log's topic, you can specify its value in conf file

## Advantage
+ `Disk Free`: the generation data will be send to AliCloud Log Service in real time through network.
+ `Without Refactor`: if your application already use Log4j, you can just add Log4j appender to your configuration file.
+ `Asynchronous and High Throughput`: the data will be send to AliCloud Log Service asynchronously. It is suitable for high concurrent write.
+ `Context Query`: at server side, in addition to searching log with keywords, you can obtain the context information of original log as well.


## Supported Version
* aliyun-log-producer 0.2.0
* protobuf-java 2.5.0


## Configuration Steps

### 1. Adding the Dependencies in pom.xml

```
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>2.5.0</version>
</dependency>
<dependency>
    <groupId>com.aliyun.openservices</groupId>
    <artifactId>aliyun-log-log4j-appender</artifactId>
    <version>0.1.12</version>
</dependency>
```

### 2. Modify the Configuration File

Take `log4j.properties` as an example, you can configure the appender and logger related to AliCloud Log Services as follows:
```
log4j.rootLogger=WARN,loghub

log4j.appender.loghub=com.aliyun.openservices.log.log4j.LoghubAppender

# Specify the project of your log services, required
log4j.appender.loghub.project=[your project]
# Specify the logStore of your log services, required
log4j.appender.loghub.logStore=[your logStore]
# Specify the HTTP endpoint of your log services, required
log4j.appender.loghub.endpoint=[your project endpoint]
# Specify the account information for your log services, required
log4j.appender.loghub.accessKeyId=[your accessKeyId]
log4j.appender.loghub.accessKeySecret=[your accessKeySecret]

# Specify format of the field log, required
log4j.appender.loghub.layout=org.apache.log4j.PatternLayout
log4j.appender.loghub.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n

# The upper limit log size that a single producer instance can hold, default is 100MB.
log4j.appender.loghub.totalSizeInBytes=104857600
# If the producer has insufficient free space, the caller's maximum blocking time on the send method, defaults is 60 seconds.
log4j.appender.loghub.maxBlockMs=60000
# The thread pool size for executing log sending tasks, defaults is the number of processors available.
log4j.appender.loghub.ioThreadCount=8
# When the size of the cached log in a Producer Batch is greater than or equal batchSizeThresholdInBytes, the batch will be send, default is 512KB, maximum can be set to 5MB.
log4j.appender.loghub.batchSizeThresholdInBytes=524288
# When the number of log entries cached in a ProducerBatch is greater than or equal to batchCountThreshold, the batch will be send.
log4j.appender.loghub.batchCountThreshold=4096
# A ProducerBatch has a residence time from creation to sending, defaulting is 2 seconds and a minimum of 100 milliseconds.
log4j.appender.loghub.lingerMs=2000
# The number of times a Producer Batch can be retried if it fails to send for the first time, default is 10.
log4j.appender.loghub.retries=10
# The backoff time for the first retry, default 100 milliseconds.
log4j.appender.loghub.baseRetryBackoffMs=100
# The maximum backoff time for retries, default is 50 seconds.
log4j.appender.loghub.maxRetryBackoffMs=100

# Specify the topic of your log, default is "", optional
log4j.appender.loghub.topic = [your topic]

# Specify the source of your log, default is host ip, optional
source = [your source]

# Specify time format of the field time, default is yyyy-MM-dd'T'HH:mm:ssZ, optional
timeFormat = yyyy-MM-dd'T'HH:mm:ssZ

# Specify timezone of the field time, default is UTC, optional
timeZone = UTC
```

## Sample Code

[Log4jAppenderExample.java](/src/main/java/com/aliyun/openservices/log/log4j/example/Log4jAppenderExample.java)

[log4j-example.properties](/src/main/resources/log4j-example.properties)

## Contributors
[@zzboy](https://github.com/zzboy) made a great contribution to this project.

Thanks for the excellent work by [@zzboy](https://github.com/zzboy).
