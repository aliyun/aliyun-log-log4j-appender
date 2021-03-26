# Log4j Appender

[![Build Status](https://travis-ci.org/aliyun/aliyun-log-log4j-appender.svg?branch=master)](https://travis-ci.org/aliyun/aliyun-log-log4j-appender)
[![License](https://img.shields.io/badge/license-Apache2.0-blue.svg)](/LICENSE)

[README in English](/README.md)

## Aliyun Log Log4j Appender
Log4j 是 Apache 的一个开放源代码项目，通过使用 Log4j，您可以控制日志信息输送的目的地是控制台、文件、GUI 组件、甚至是套接口服务器、NT 的事件记录器、UNIX Syslog 守护进程等；您也可以控制每一条日志的输出格式；通过定义每一条日志信息的级别，您能够更加细致地控制日志的生成过程。最令人感兴趣的就是，这些可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。

Log4j 由三个重要的组件构成：日志信息的优先级，日志信息的输出目的地，日志信息的输出格式。日志信息的优先级从高到低分别为 ERROR、WARN、INFO和DEBUG，分别用来指定这条日志信息的重要程度；日志信息的输出目的地指定了日志将打印到控制台还是文件中；而输出格式则控制了日志信息的显示内容。

通过Aliyun Log Log4j Appender，您可以控制日志的输出目的地为阿里云日志服务。写到日志服务中的日志的样式如下：
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
其中：
+ level 日志级别。
+ location 日志打印语句的代码位置。
+ message 日志内容。
+ throwable 日志异常信息（只有记录了异常信息，这个字段才会出现）。
+ thread 线程名称。
+ time 日志打印时间（可以通过 timeFormat 或 timeZone 配置 time 字段呈现的格式和时区）。
+ log 自定义日志格式。
+ \_\_source\_\_ 日志来源，用户可在配置文件中指定。
+ \_\_topic\_\_ 日志主题，用户可在配置文件中指定。

## 功能优势
+ 日志不落盘：产生数据实时通过网络发给服务端。
+ 无需改造：对已使用Log4J应用，只需简单配置即可采集。
+ 异步高吞吐：高并发设计，后台异步发送，适合高并发写入。
+ 上下文查询：服务端除了通过关键词检索外，给定日志能够精确还原原始日志文件上下文日志信息。

## 版本支持
* aliyun-log-producer 0.2.0
* protobuf-java 2.5.0


## 配置步骤

### 1. maven 工程中引入依赖

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

### 2. 修改配置文件

以配置文件`log4j.properties`为例（不存在则在项目根目录创建），配置Loghub相关的appender与 Logger，例如：
```
log4j.rootLogger=WARN,loghub

log4j.appender.loghub=com.aliyun.openservices.log.log4j.LoghubAppender

#日志服务的project名，必选参数
log4j.appender.loghub.project=[your project]
#日志服务的logstore名，必选参数
log4j.appender.loghub.logStore=[your logStore]
#日志服务的http地址，必选参数
log4j.appender.loghub.endpoint=[your project endpoint]
#用户身份标识，必选参数
log4j.appender.loghub.accessKeyId=[your accesskey id]
log4j.appender.loghub.accessKeySecret=[your accessKeySecret]

#设置 log 字段格式，必选参数
log4j.appender.loghub.layout=org.apache.log4j.PatternLayout
log4j.appender.loghub.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n

#单个 producer 实例能缓存的日志大小上限，默认为 100MB。
log4j.appender.loghub.totalSizeInBytes=104857600
#如果 producer 可用空间不足，调用者在 send 方法上的最大阻塞时间，默认为 60 秒。为了不阻塞打印日志的线程，强烈建议将该值设置成 0。
log4j.appender.loghub.maxBlockMs=0
#执行日志发送任务的线程池大小，默认为可用处理器个数。
log4j.appender.loghub.ioThreadCount=8
#当一个 ProducerBatch 中缓存的日志大小大于等于 batchSizeThresholdInBytes 时，该 batch 将被发送，默认为 512 KB，最大可设置成 5MB。
log4j.appender.loghub.batchSizeThresholdInBytes=524288
#当一个 ProducerBatch 中缓存的日志条数大于等于 batchCountThreshold 时，该 batch 将被发送，默认为 4096，最大可设置成 40960。
log4j.appender.loghub.batchCountThreshold=4096
#一个 ProducerBatch 从创建到可发送的逗留时间，默认为 2 秒，最小可设置成 100 毫秒。
log4j.appender.loghub.lingerMs=2000
#如果某个 ProducerBatch 首次发送失败，能够对其重试的次数，默认为 10 次。
#如果 retries 小于等于 0，该 ProducerBatch 首次发送失败后将直接进入失败队列。
log4j.appender.loghub.retries=10
#该参数越大能让您追溯更多的信息，但同时也会消耗更多的内存。
log4j.appender.loghub.maxReservedAttempts=11
#首次重试的退避时间，默认为 100 毫秒。
#Producer 采样指数退避算法，第 N 次重试的计划等待时间为 baseRetryBackoffMs * 2^(N-1)。
log4j.appender.loghub.baseRetryBackoffMs=100
#重试的最大退避时间，默认为 50 秒。
log4j.appender.loghub.maxRetryBackoffMs=50000

#指定日志主题，默认为 ""，可选参数
log4j.appender.loghub.topic = [your topic]

#指的日志来源，默认为应用程序所在宿主机的 IP，可选参数
log4j.appender.loghub.source = [your source]

#设置时间格式，默认为 yyyy-MM-dd'T'HH:mm:ssZ，可选参数
log4j.appender.loghub.timeFormat=yyyy-MM-dd'T'HH:mm:ssZ
#设置时区，默认为 UTC，可选参数（如果希望 time 字段的时区为东八区，可将该值设定为 Asia/Shanghai）
log4j.appender.loghub.timeZone=UTC
```
参阅：https://github.com/aliyun/aliyun-log-producer-java

## 使用实例

项目中提供了一个名为`com.aliyun.openservices.log.log4j.Log4jAppenderExample`的实例，它会加载resources目录下的`log4j.properties`文件进行log4j配置。

**log4j.properties样例说明**
+ 配置了三个appender：loghubAppender1、loghubAppender2、STDOUT。
+ loghubAppender1：将日志输出到project=test-proj，logStore=store1。输出WARN及以上级别的日志。
+ loghubAppender2：将日志输出到project=test-proj，logStore=store2。输出INFO及以上级别的日志。
+ STDOUT：将日志输出到控制台。由于没有对日志级别进行过滤，会输出rootLogger中配置的日志级及以上的所有日志。

[Log4jAppenderExample.java](/src/main/java/com/aliyun/openservices/log/log4j/example/Log4jAppenderExample.java)

[log4j-example.properties](/src/main/resources/log4j-example.properties)

## 错误诊断

如果您发现数据没有写入日志服务，可通过如下步骤进行错误诊断。
1. 检查配置文件 log4j.properties 是否限定了 appender 只输出特定级别的日志。比如，是否设置了 root，logger 或 appender 的 level 属性。
2. 检查您项目中引入的 protobuf-java，aliyun-log-log4j-appender 这两个 jar 包的版本是否和文档中`maven 工程中引入依赖`部分列出的 jar 包版本一致。
3. 通过观察控制台的输出来诊断您的问题。Aliyun Log Log4j Appender 会将 appender 运行过程中产生的异常通过 `org.apache.log4j.helpers.LogLog` 记录下来，LogLog 在默认情况下会将信息输出到控制台。查看控制台是否包含 `Failed to putLogs.`。

## 常见问题
**Q**：是否支持自定义 log 格式？

**A**：在 0.1.10 及以上版本新增了 log 字段。您可以通过配置 layout 来自定义 log 格式，例如：
```
#设置 log 字段格式，必选参数
log4j.appender.loghub.layout=org.apache.log4j.PatternLayout
log4j.appender.loghub.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n
```
log 输出样例：
```
log:  0 [main] ERROR com.aliyun.openservices.log.log4j.example.Log4jAppenderExample - error log
```

**Q**: 如何关闭某些类输出的日志？

**A**: 通过在 log4j.properties 文件中添加 `log4j.logger.包名=OFF` 可屏蔽相应包下日志的输出。
例如，当您在 log4j.properties 文件中添加如下内容会屏蔽 package 名为 `org.apache.http` 下所有类的日志输出。
```
log4j.logger.org.apache.http=OFF
```

**Q**: 如何采集宿主机 IP？

**A**: 不要在 log4j.properties 中设置 source 字段的值，这种情况下 source 字段会被设置成应用程序所在宿主机的 IP。

**Q**：用户可以自定义 `source` 字段的取值吗？

**A**：0.1.7 以及之前的版本不支持，在这些版本中 source 字段会被设置成应用程序所在宿主机的 IP。在最新的版本中，您可以参考上面的配置文件指定 source 的取值。

**Q**：用户可以指定 `time` 字段的取值吗？

**A**：用户无法指定 `time` 取值，appender 会以当前时间作为 `time` 的值。

**Q**：在网络发生异常的情况下，`aliyun-log-log4j-appender` 会如何处理待发送的日志？

**A**：`aliyun-log-log4j-appender` 底层使用 `aliyun-log-producer-java` 发送数据。producer 会根据您在配置文件中设置的 `retryTimes` 进行重试，如果超过 `retryTimes` 次数据仍没有发送成功，会将错误信息输出，并丢弃该条日志。关于如何查看错误输出，可以参考错误诊断部分。

**Q**：如果想设置 `time` 字段的时区为东八区或其他时区，该如何指定 `timeZone` 的取值？

**A**：当您将 `timeZone` 指定为 `Asia/Shanghai` 时，`time` 字段的时区将为东八区。timeZone 字段可能的取值请参考 [java-util-timezone](http://tutorials.jenkov.com/java-date-time/java-util-timezone.html)。

## 贡献者
[@zzboy](https://github.com/zzboy) 对项目作了很大贡献。

感谢 [@zzboy](https://github.com/zzboy) 的杰出工作。
