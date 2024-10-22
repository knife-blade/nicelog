# nicelog

## 1.介绍

nicelog：功能强大的Java日志组件。

## 2.功能

**1. 手动打印**

手动打印日志。

**2. 自动收集日志**

在三处收集日志：进入时、返回时、报异常时。

默认情况下，只要你的项目里有相关的组件，比如XXL-JOB，就会自动收集其日志。

当前支持的组件有：

1. Controller
2. XXL-JOB
3. Bean的方法或者类上加@NiceLog注解
4. Feign
5. RabbitMQ
6. RocketMQ
7. Kafka
8. Scheduled

**3. 更多功能**

准备支持：我自己暂时想不到了。如果有需求请提issue

## 3.快速开始

**1. 引入依赖**

```xml
<dependency>
    <groupId>com.suchtool</groupId>
    <artifactId>nicelog-spring-boot-starter</artifactId>
    <version>{newest-version}</version>
</dependency>
```

**2.使用示例**

```
@Api(tags = "测试类")
@RequestMapping("test")
@RestController
public class HelloController {

    @ApiOperation("测试1")
    @PostMapping("test1")
    public String test(User user, String email) {
        NiceLogUtil.createBuilder()
                .mark("我的打印")
                .info();
        return "success";
    }
}
```

日志输出结果：
```
2024-02-29 19:15:20.607  INFO 5840 --- [nio-8080-exec-1] c.s.n.p.impl.NiceLogProcessDefaultImpl   : nicelog日志：{"param":"{\"userName\":\"Tony\"}","returnValue":null,"mark":null,"businessNo":null,"errorInfo":null,"throwable":null,"other1":null,"other2":null,"other3":null,"other4":null,"other5":null,"other6":null,"other7":null,"other8":null,"other9":null,"other10":null,"appName":"","entryType":"CONTROLLER","entry":"/test/test1","entryClassTag":"测试","entryMethodTag":"测试1","className":"com.knife.example.controller.HelloController","classTag":"测试","methodName":"test","methodTag":"测试1","methodDetail":"com.knife.example.controller.HelloController.test(java.lang.String)","codeLineNumber":null,"level":"INFO","directionType":"IN","traceId":"3d250d34d8914de5b847fea3ba93d7a7","logTime":"2024-02-29 19:15:20.591","clientIp":"10.0.10.110","ip":"10.0.10.110"}
2024-02-29 19:15:20.619  INFO 5840 --- [nio-8080-exec-1] c.s.n.p.impl.NiceLogProcessDefaultImpl   : nicelog日志：{"param":null,"returnValue":null,"mark":"我的打印","businessNo":null,"errorInfo":null,"throwable":null,"other1":null,"other2":null,"other3":null,"other4":null,"other5":null,"other6":null,"other7":null,"other8":null,"other9":null,"other10":null,"appName":"","entryType":"MANUAL","entry":"/test/test1","entryClassTag":"测试","entryMethodTag":"测试1","className":"com.knife.example.controller.HelloController","classTag":null,"methodName":"test","methodTag":null,"methodDetail":null,"codeLineNumber":"23","level":"INFO","directionType":"INNER","traceId":"3d250d34d8914de5b847fea3ba93d7a7","logTime":"2024-02-29 19:15:20.619","clientIp":"10.0.10.110","ip":"10.0.10.110"}
2024-02-29 19:15:20.620  INFO 5840 --- [nio-8080-exec-1] c.s.n.p.impl.NiceLogProcessDefaultImpl   : nicelog日志：{"param":null,"returnValue":"\"success\"","mark":null,"businessNo":null,"errorInfo":null,"throwable":null,"other1":null,"other2":null,"other3":null,"other4":null,"other5":null,"other6":null,"other7":null,"other8":null,"other9":null,"other10":null,"appName":"","entryType":"CONTROLLER","entry":"/test/test1","entryClassTag":"测试","entryMethodTag":"测试1","className":"com.knife.example.controller.HelloController","classTag":"测试","methodName":"test","methodTag":"测试1","methodDetail":"com.knife.example.controller.HelloController.test(java.lang.String)","codeLineNumber":null,"level":"INFO","directionType":"OUT","traceId":"3d250d34d8914de5b847fea3ba93d7a7","logTime":"2024-02-29 19:15:20.620","clientIp":"10.0.10.110","ip":"10.0.10.110"}
```

## 4 使用说明

**1. 打印日志**

默认情况下，会通过logback输出（默认实现为：NiceLogProcessDefaultImpl）。

支持自定义处理日志：提供一个Bean，实现com.suchtool.nicelog.process.NiceLogProcess的void process(NiceLogInnerBO niceLogInnerBO)方法即可。
例如：
```
@Component
public class CustomLogProcessor implements NiceLogProcess {
    @Override
    public void process(NiceLogInnerBO logInnerBO) {
        // 这里可以这么做：
        // 1.取出logInnerBO的字段值，赋值到项目本身的日志实体类
        // 2.打印到控制台或者上传到ES等
    }
}
```

**2. 自动收集日志**

自动收集相关组件的日志。原理：使用AOP。

**3. 手动打印日志**

支持手动打印日志：
```
NiceLogUtil.createBuilder()
        .mark("创建订单")
        .info();
```

此工具支持通过lambda的形式构造参数并打印，每次输入.都会有代码提示，类似于MyBatis-Plus的lambdaQuery。

**4. 手动收集日志**

在方法或者是类上加@NiceLog，即可收集出入参、返回值、异常信息。

注意：此类必须注入Spring，方法必须是public。

## 5.详细配置

### 5.1 yml配置

支持SpringBoot的配置文件进行配置，比如：application.yml。

| 配置                                          | 描述                   | 默认值  |
|---------------------------------------------|----------------------|------|
| suchtool.nicelog.enabled                    | 启用日志                 | true |
| suchtool.nicelog.collect-all                | 收集所有                 | true |
| suchtool.nicelog.enable-controller-log      | 启用Controller日志       | true |
| suchtool.nicelog.enable-xxl-job-log         | 启用XXL-JOB日志          | true |
| suchtool.nicelog.enable-scheduled-log       | 启用@Scheduled日志       | true |
| suchtool.nicelog.enable-nice-log-annotation-log | 启用@NiceLog日志        | true |
| suchtool.nicelog.enable-rabbit-mq-log          | 启用RabbitMQ日志         | true |
| suchtool.nicelog.enable-rocket-mq-log          | 启用RocketMQ日志         | true |
| suchtool.nicelog.enable-kafka-log              | 启用Kafka日志            | true |
| suchtool.nicelog.enable-feign-log              | 启用Feign日志            | true |
| suchtool.nicelog.ignore-feign-log-package-name | 不收集Feign日志的包名，多个用逗号隔开 | 空  |
| suchtool.nicelog.feign-trace-id-header         | feign的traceId的header名字 | nice-log-trace-id |
### 5.2 设置优先级
日志自动收集功能是通过AOP实现的，你可以用SpringBoot的配置文件指定它们的优先级：

| 配置                                        | 描述                   | 默认值  |
|---------------------------------------------|----------------------|------|
| suchtool.nicelog.controller-log-order         | Controller接口日志的顺序 | 20000 |
| suchtool.nicelog.xxl-job-log-order            | XxlJob日志的顺序         | 20000 |
| suchtool.nicelog.rabbit-mq-log-order          | RabbitMQ日志的顺序       | 20000 |
| suchtool.nicelog.rocket-mq-log-order          | RocketMQ日志的顺序      | 20000 |
| suchtool.nicelog.kafka-log-order             | Kafka日志的顺序         | 20000 |
| suchtool.nicelog.nice-log-annotation-log-order | NiceLog注解日志的顺序    | 20000 |
| suchtool.nicelog.feign-log-order             | Feign日志的顺序          | 20000 |
| suchtool.nicelog.feign-request-interceptorOrder | Feign请求拦截器的顺序  | 20000 |
| suchtool.nicelog.scheduled-log-order             | Scheduled日志的顺序  | 20000 |

### 5.3 日志开关
默认会自动收集所有支持组件的日志。可以自由的开关：

**场景1：不收集某个组件**
假如不收集Kafka的日志，就这样配置yml：suchtool.nicelog.enable-kafka-log=false

**场景2：关闭所有组件，只收集标有@NiceLog的类或方法**
配置yml：suchtool.nicelog.collect-all=false

**场景3：不收集某个类或方法**
在类或者方法上加注解：@NiceLogIgnore

### 5.4 收集Feign原始响应body
如果你没有自定义Feign的Decoder，是能够收集到原始响应body的。
如果你自定义了Feign的Decoder，则需要手动保存一下body，这样后边就能打印出来，例如：
```
@Configuration
public class FeignLogResponseDecoder extends SpringDecoder {
    public FeignLogResponseDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public Object decode(final Response response, Type type) throws IOException, FeignException {
        Response.Body body = response.body();
        String bodyString = StreamUtils.copyToString(body.asInputStream(), StandardCharsets.UTF_8);
        // 这里将body保存下来
        NiceLogFeignContextThreadLocal.saveOriginFeignResponseBody(bodyString);

        // body流只能读一次，必须重新封装一下
        Response newResponse = response.toBuilder().body(bodyString, StandardCharsets.UTF_8).build();
        return super.decode(newResponse, type);
    }
}
```

## 6. 字段的含义
| Key                    | 含义             | 备注 |
|------------------------|----------------|--|
| param                  | 入参             | 手动时可自定义 |
| returnValue            | 返回值            | 手动时可自定义 |
| originReturnValue      | 原始返回值          | 手动时可自定义 |
| mark                   | 标记             | 手动时可自定义 |
| businessNo             | 业务单号           | 手动时可自定义 |
| message                | 信息             | 手动时可自定义 |
| errorInfo              | 错误信息           | 手动时可自定义 |
| errorDetailInfo        | 错误详细信息         | 手动时可自定义 |
| throwable              | Throwable异常类   | 手动时可自定义。栈追踪字符串会自动保存到NiceLogInnerBO.stackTrace |
| recordStackTrace       | 记录栈追踪          | 手动时可自定义。用于非异常时主动获得栈追踪，会将栈追踪字符串会保存到NiceLogInnerBO.stackTrace。若throwable不为空，则使用throwable的栈数据 |
| appName                | 应用名字           | 取的是spring.application.name配置 |
| groupName              | 组名字           | 用于区分应用所在的组，建议放到公共组件里指定 |
| entryType              | 入口类型           | MANUAL：手动；CONTROLLER：接口；RABBIT_MQ：RabbitMQ；XXL_JOB：XXL-JOB；NICE_LOG_ANNOTATION：NiceLog注解；FEIGN：Feign; ROCKETMQ：RocketMQ；KAFKA：Kafka |
| entry                  | 入口             | 对于Controller，是URL；对于RabbitMQ，是@RabbitListener的queues；对于XXL-JOB，是@XxlJob的value；对于Feign，是URL；对于RocketMQ，是@RocketMQMessageListener的topic字段；对于Kafka，是@KafkaListener的topics字段。作为上下文传递。 |
| entryClassTag          | 入口类的tag        | 取值优先级为：先取@NiceLog的value，若为空则取：对于Controller：Controller类上的@Api的tags > Controller类上的@Api的value；对于Feign：@FeignClient的value字段。作为上下文传递。 |
| entryMethodTag         | 入口方法的tag       | 取值优先级为：@NiceLog的value > Controller方法上的@ApiOperation的value。作为上下文传递。 |
| className              | 类名             | |
| classTag               | 当前类的tag        | 取值同entryClassTag，但不作为上下文传递。 |
| methodName             | 方法名            | |
| methodTag              | 当前方法的tag       | 取值同entryMethodTag，但不作为上下文传递。 |
| methodDetail           | 方法详情           | 全限定类名+方法名+全限定参数 |
| lineNumber             | 代码行号           | 只在手动输出时有值。 |
| classNameAndLineNumber | 类名及代码行号，中间用:隔开 | 只在手动输出时有值。 |
| level                  | 级别             | INFO、WARNING、ERROR |
| directionType          | 方向             | IN：方法进入；OUT：方法退出；INNER：方法内部执行 |
| traceId                | 链路id           | 作为上下文传递 |
| stackTrace             | 栈追踪字符串         | |
| logTime                | 日志时间           | |
| clientIp               | 客户端IP          | |
| ip                     | 调用方IP          | |
| other1                 | 备用字段1          | 手动时可自定义 |
| other2                 | 备用字段2          | 手动时可自定义 |
| other3                 | 备用字段3          | 手动时可自定义 |
| other4                 | 备用字段4          | 手动时可自定义 |
| other5                 | 备用字段5          | 手动时可自定义 |
| other6                 | 备用字段6          | 手动时可自定义 |
| other7                 | 备用字段7          | 手动时可自定义 |
| other8                 | 备用字段8          | 手动时可自定义 |
| other9                 | 备用字段9          | 手动时可自定义 |
| other10                | 备用字段10         | 手动时可自定义 |

