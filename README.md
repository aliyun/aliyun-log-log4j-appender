# Log4j Appender

[![Build Status](https://travis-ci.org/aliyun/aliyun-log-log4j-appender.svg?branch=master)](https://travis-ci.org/aliyun/aliyun-log-log4j-appender)
[![License](https://img.shields.io/badge/license-Apache2.0-blue.svg)](/LICENSE)

[中文版README](/README_CN.md)

## Aliyun Log Log4j Appender

Apache log4j is an Apache Software Foundation Project. You can control the destination of the log through Log4j. It can be console, file, GUI components, socket, NT event log, syslog. You can control the output format for each log as well. You can control the generation process of the log through log level. The most interesting thing is you can complete the above things through a configuration file and without any code modification.

You can set the destination of your log to AliCloud Log Service through `Aliyun Log Log4j Appender`. But it is important to note that `Aliyun Log Log4j Appender` doesn't support cofigure log's output format. The format of the log in AliCloud Log Service is as follows:
```
level: ERROR
location: com.aliyun.openservices.log.log4j.example.Log4jAppenderExample.main(Log4jAppenderExample.java:16)
message: error log
thread: main
time: 2018-01-02T03:15+0000
__source__: xxx
__topic__: yyy
```
Field Specifications:
+ `level` stands for log level
+ `location` is logs's output position
+ `message` is the content of the log
+ `thread` stands for thread name
+ `time` is the log's generation time
+ `__source__` is the log's source, you can specify its value in conf file
+ `__topic__` is the log's topic, you can specify its value in conf file

## Advantage
+ `Disk Free`: the generation data will be send to AliCloud Log Service in real time through network.
+ `Without Refactor`: if your application already use Log4j, you can just add Log4j appender to your configuration file.
+ `Asynchronous and High Throughput`: the data will be send to AliCloud Log Service asynchronously. It is suitable for high concurrent write.
+ `Context Query`: at server side, in addition to searching log with keywords, you can obtain the context information of original log as well.


## Supported Version
* log-loghub-producer 0.1.10
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
    <version>0.1.9</version>
</dependency>
```

### 2. Modify the Configuration File

Take `log4j.properties` as an example, you can configure the appender and logger related to AliCloud Log Services as follows:
```
log4j.rootLogger=WARN,loghub

log4j.appender.loghub=com.aliyun.openservices.log.log4j.LoghubAppender

# Specify the project name of your log services, required
log4j.appender.loghub.projectName=[your project]
# Specify the logstore of your log services, required
log4j.appender.loghub.logstore=[your logstore]
# Specify the HTTP endpoint of your log services, required
log4j.appender.loghub.endpoint=[your project endpoint]
# Specify the account information for your log services, required
log4j.appender.loghub.accessKeyId=[your accesskey id]
log4j.appender.loghub.accessKey=[your accesskey]

# Specify the timeout for sending package, in milliseconds, default is 3000, the lower bound is 10, optional
log4j.appender.loghub.packageTimeoutInMS=3000
# Specify the maximum log count per package, the upper limit is 4096, optional
log4j.appender.loghub.logsCountPerPackage=4096
# Specify the maximum cache size per package, the upper limit is 3MB, in bytes, optional
log4j.appender.loghub.logsBytesPerPackage=3145728
# The upper limit of the memory that can be used by appender, in bytes, default is 100MB, optional
log4j.appender.loghub.memPoolSizeInByte=1048576000
# Specify the I/O thread pool's maximum pool size, the main function of the I/O thread pool is to send data, default is 8, optional
maxIOThreadSizeInPool = 8
log4j.appender.loghub.maxIOThreadSizeInPool=8
# Specify the retry times when failing to send data, if exceeds this value, the appender will record the failure message through it's LogLog, default is 3, optional
log4j.appender.loghub.retryTimes=3

# Specify the topic of your log
log4j.appender.loghub.topic = [your topic]

# Specify the source of your log
source = [your source]

# Specify the time format of the data being sent to AliCloud Log Service, use SimpleDateFormat in Java to format time, default is ISO8601，optional
log4j.appender.loghub.timeFormat=yyyy-MM-dd'T'HH:mmZ
log4j.appender.loghub.timeZone=UTC
```

## Sample Code

[Log4jAppenderExample.java](/src/main/java/com/aliyun/openservices/log/log4j/example/Log4jAppenderExample.java)

[log4j-example.properties](/src/main/resources/log4j-example.properties)

## Contributors
[@zzboy](https://github.com/zzboy) made a great contribution to this project.

Thanks for the excellent work by [@zzboy](https://github.com/zzboy).