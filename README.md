# Loghub Log4j Appender

Log4j 是 Apache 的一个开放源代码项目，通过使用 Log4j，您可以控制日志信息输送的目的地是控制台、文件、GUI 组件、甚至是套接口服务器、NT 的事件记录器、UNIX Syslog 守护进程等；您也可以控制每一条日志的输出格式；通过定义每一条日志信息的级别，您能够更加细致地控制日志的生成过程。最令人感兴趣的就是，这些可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。

Log4j 由三个重要的组件构成：日志信息的优先级，日志信息的输出目的地，日志信息的输出格式。日志信息的优先级从高到低有 ERROR、WARN、INFO、DEBUG，分别用来指定这条日志信息的重要程度；日志信息的输出目的地指定了日志将打印到控制台还是文件中；而输出格式则控制了日志信息的显示内容。

使用 Loghub Log4j Appender，您可以控制日志的输出目的地为阿里云日志服务，有一点需要特别注意，Loghub Log4j Appender 不支持设置日志的输出格式，写到日志服务中的日志的样式如下：
```
level:ERROR
location:test.TestLog4jAppender.main(TestLog4jAppender.java:18)
message:test log4j appender
thread:main
time:2016-05-27T03:15+0000
```
其中：

+ level 是日志级别。
+ location 是日志打印语句的代码位置。
+ message 是日志内容。
+ thread 是线程名称。
+ time 是日志打印时间。
# Loghub Log4j Appender 的优势

客户端日志不落盘：即数据生产后直接通过网络发往服务端。
对于已经使用 log4j 记录日志的应用，只需要简单修改配置文件就可以将日志传输到日志服务。
异步高吞吐，Loghub Log4j Appender 会将用户的日志 merge 之后异步发送，提高网络 IO 效率。

# 版本支持
+ log-loghub-producer 0.1.8
+ protobuf-java 2.5.0

# 配置步骤

1. maven 工程中引入依赖

```
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>2.5.0</version>
</dependency>
<dependency>
     <groupId>com.aliyun.openservices</groupId>
     <artifactId>log-loghub-log4j2-appender</artifactId>
     <version>0.1.0</version>
</dependency>
```
2. 修改配置文件：以配置文件`log4j2.properties`为例（不存在则在项目根目录创建），配置Loghub相关的appender与 Logger，例如：
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
#当使用临时身份时必须填写，非临时身份则不需要填写
log4j.appender.loghub.stsToken=[your ststoken]

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