# Log4j Appender

## Aliyun Log Log4j Appender
Log4j 是 Apache 的一个开放源代码项目，通过使用 Log4j，您可以控制日志信息输送的目的地是控制台、文件、GUI 组件、甚至是套接口服务器、NT 的事件记录器、UNIX Syslog 守护进程等；您也可以控制每一条日志的输出格式；通过定义每一条日志信息的级别，您能够更加细致地控制日志的生成过程。最令人感兴趣的就是，这些可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。

Log4j 由三个重要的组件构成：日志信息的优先级，日志信息的输出目的地，日志信息的输出格式。日志信息的优先级从高到低分别为 ERROR、WARN、INFO和DEBUG，分别用来指定这条日志信息的重要程度；日志信息的输出目的地指定了日志将打印到控制台还是文件中；而输出格式则控制了日志信息的显示内容。

通过Aliyun Log Log4j Appender，您可以控制日志的输出目的地为阿里云日志服务。需要注意的是，Aliyun Log Log4j Appender不支持设置日志的输出格式，写到日志服务中的日志的样式如下：
```
level: ERROR
location: com.aliyun.openservices.log.log4j.example.Log4jAppenderExample.main(Log4jAppenderExample.java:16)
message: error log
thread: main
time: 2018-01-02T03:15+0000
```
其中：
+ level 是日志级别。
+ location 是日志打印语句的代码位置。
+ message 是日志内容。
+ thread 是线程名称。
+ time 是日志打印时间。

## 功能优势
+ 日志不落盘：产生数据实时通过网络发给服务端。
+ 无需改造：对已使用Log4J应用，只需简单配置即可采集。
+ 异步高吞吐：高并发设计，后台异步发送，适合高并发写入。
+ 上下文查询：服务端除了通过关键词检索外，给定日志能够精确还原原始日志文件上下文日志信息。

## 版本支持
* log-loghub-producer 0.1.8
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
    <version>0.1.4</version>
</dependency>
```

### 2. 修改配置文件

以配置文件`log4j.properties`为例（不存在则在项目根目录创建），配置Loghub相关的appender与 Logger，例如：
```
log4j.rootLogger=WARN,loghub

log4j.appender.loghub=com.aliyun.openservices.log.log4j.LoghubAppender

#日志服务的project名，必选参数
log4j.appender.loghub.projectName=[your project]
#日志服务的logstore名，必选参数
log4j.appender.loghub.logstore=[your logstore]
#日志服务的http地址，必选参数
log4j.appender.loghub.endpoint=[your project endpoint]
#用户身份标识，必选参数
log4j.appender.loghub.accessKeyId=[your accesskey id]
log4j.appender.loghub.accessKey=[your accesskey]

#被缓存起来的日志的发送超时时间，如果缓存超时，则会被立即发送，单位是毫秒，默认值为3000，最小值为10，可选参数
log4j.appender.loghub.packageTimeoutInMS=3000
#每个缓存的日志包中包含日志数量的最大值，不能超过 4096，可选参数
log4j.appender.loghub.logsCountPerPackage=4096
#每个缓存的日志包的大小的上限，不能超过 5MB，单位是字节，可选参数
log4j.appender.loghub.logsBytesPerPackage=5242880
#Appender 实例可以使用的内存的上限，单位是字节，默认是 100MB，可选参数
log4j.appender.loghub.memPoolSizeInByte=1048576000
#指定I/O线程池最大线程数量，主要用于发送数据到日志服务，默认是8，可选参数
log4j.appender.loghub.maxIOThreadSizeInPool=8
#指定发送失败时重试的次数，如果超过该值，会把失败信息记录到log4j的StatusLogger里，默认是3，可选参数
log4j.appender.loghub.retryTimes=3

#指定日志主题
log4j.appender.loghub.topic = [your topic]

#输出到日志服务的时间格式，使用 Java 中 SimpleDateFormat 格式化时间，默认是 ISO8601，可选参数
log4j.appender.loghub.timeFormat=yyyy-MM-dd'T'HH:mmZ
log4j.appender.loghub.timeZone=UTC
```
参阅：https://help.aliyun.com/document_detail/43758.html

## 参数说明

Aliyun Log Log4j Appender 可供配置的属性（参数）如下，其中注释为必选参数的是必须填写的，可选参数在不填写的情况下，使用默认值。

```
#日志服务的 project 名，必选参数
projectName = [your project]
#日志服务的 logstore 名，必选参数
logstore = [your logstore]
#日志服务的 HTTP 地址，必选参数
endpoint = [your project endpoint]
#用户身份标识，必选参数
accessKeyId = [your accesskey id]
accessKey = [your accesskey]

#被缓存起来的日志的发送超时时间，如果缓存超时，则会被立即发送，单位是毫秒，默认值为3000，最小值为10，可选参数
packageTimeoutInMS = 3000
#每个缓存的日志包中包含日志数量的最大值，不能超过 4096，可选参数
logsCountPerPackage = 4096
#每个缓存的日志包的大小的上限，不能超过 5MB，单位是字节，可选参数
logsBytesPerPackage = 5242880
#Appender 实例可以使用的内存的上限，单位是字节，默认是 100MB，可选参数
memPoolSizeInByte = 1048576000
#指定I/O线程池最大线程数量，主要用于发送数据到日志服务，默认是8，可选参数
maxIOThreadSizeInPool = 8
#指定发送失败时重试的次数，如果超过该值，会把失败信息记录到log4j的StatusLogger里，默认是3，可选参数
retryTimes = 3

#指定日志主题
topic = [your topic]

#输出到日志服务的时间格式，使用 Java 中 SimpleDateFormat 格式化时间，默认是 ISO8601，可选参数
timeFormat = yyyy-MM-dd'T'HH:mmZ
timeZone  UTC
```
参阅：https://help.aliyun.com/document_detail/43758.html


## 使用实例
项目中提供了一个名为`com.aliyun.openservices.log.log4j.Log4jAppenderExample`的实例，它会加载resources目录下的`log4j.properties`文件进行log4j配置。

**log4j.properties样例说明**
+ 配置了三个appender：loghubAppender1、loghubAppender2、STDOUT。
+ loghubAppender1：将日志输出到project=test-proj，logstore=store1。输出WARN及以上级别的日志。
+ loghubAppender2：将日志输出到project=test-proj，logstore=store2。输出INFO及以上级别的日志。
+ STDOUT：将日志输出到控制台。由于没有对日志级别进行过滤，会输出rootLogger中配置的日志级及以上的所有日志。

[Log4jAppenderExample.java](/src/main/java/com/aliyun/openservices/log/log4j/example/Log4jAppenderExample.java)

[log4j.properties](/src/main/resources/log4j.properties)
