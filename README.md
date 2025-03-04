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
3. Feign
4. RabbitMQ
5. RocketMQ
6. Kafka
7. Scheduled
8. Bean的类或方法上加@NiceLog注解

**3. 更多功能**

准备支持：暂时没有。如果有需求请提issue

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
@Api(tags = "用户管理")
@RequestMapping("user")
@RestController
public class UserController {
    @ApiOperation(value = "注册")
    @PostMapping("register")
    public String register(@RequestBody UserBO userBO) {
        NiceLogUtil.createBuilder()
                .mark("我的打印")
                .info();
        return "成功";
    }
}
```

日志输出结果：
```
2024-12-23 19:08:41.100  INFO 111144 --- [nio-8080-exec-1] c.s.n.p.impl.NiceLogProcessDefaultImpl   : nicelog日志：{"mark":null,"businessNo":null,"message":null,"errorInfo":null,"errorDetailInfo":null,"recordStackTrace":null,"param":"{\"userBO\":{\"id\":null,\"username\":\"Tony\",\"userContact\":null}}","returnValue":null,"originReturnValue":null,"operatorId":null,"operatorName":null,"other1":null,"other2":null,"other3":null,"other4":null,"other5":null,"other6":null,"other7":null,"other8":null,"other9":null,"other10":null,"traceId":"a3be0cccb2fb469380e2047fa972ccf7","logTime":"2024-12-23T09:08:41.092","level":"INFO","directionType":"IN","entryType":"CONTROLLER","entry":"/user/register","entryClassTag":"用户管理","entryMethodTag":"注册","className":"com.knife.example.controller.UserController","classTag":"用户管理","methodName":"register","methodTag":"注册","methodDetail":"com.knife.example.controller.UserController.register(com.knife.example.bo.UserBO)","lineNumber":null,"classNameAndLineNumber":null,"stackTrace":null,"appName":"","groupName":null,"clientIp":"12.34.5.6","callerIp":"12.34.5.6","hostIp":"12.34.5.6"}
2024-12-23 19:08:41.113  INFO 111144 --- [nio-8080-exec-1] c.s.n.p.impl.NiceLogProcessDefaultImpl   : nicelog日志：{"mark":"执行注册","businessNo":null,"message":null,"errorInfo":null,"errorDetailInfo":null,"recordStackTrace":null,"param":null,"returnValue":null,"originReturnValue":null,"operatorId":null,"operatorName":null,"other1":null,"other2":null,"other3":null,"other4":null,"other5":null,"other6":null,"other7":null,"other8":null,"other9":null,"other10":null,"traceId":"a3be0cccb2fb469380e2047fa972ccf7","logTime":"2024-12-23T09:08:41.113","level":"INFO","directionType":"INNER","entryType":"MANUAL","entry":"/user/register","entryClassTag":"用户管理","entryMethodTag":"注册","className":"com.knife.example.controller.UserController","classTag":null,"methodName":"register","methodTag":null,"methodDetail":null,"lineNumber":"23","classNameAndLineNumber":"com.knife.example.controller.UserController:23","stackTrace":null,"appName":"","groupName":null,"clientIp":null,"callerIp":null,"hostIp":"12.34.5.6"}
2024-12-23 19:08:41.114  INFO 111144 --- [nio-8080-exec-1] c.s.n.p.impl.NiceLogProcessDefaultImpl   : nicelog日志：{"mark":null,"businessNo":null,"message":null,"errorInfo":null,"errorDetailInfo":null,"recordStackTrace":null,"param":null,"returnValue":"\"注册成功\"","originReturnValue":null,"operatorId":null,"operatorName":null,"other1":null,"other2":null,"other3":null,"other4":null,"other5":null,"other6":null,"other7":null,"other8":null,"other9":null,"other10":null,"traceId":"a3be0cccb2fb469380e2047fa972ccf7","logTime":"2024-12-23T09:08:41.114","level":"INFO","directionType":"OUT","entryType":"CONTROLLER","entry":"/user/register","entryClassTag":"用户管理","entryMethodTag":"注册","className":"com.knife.example.controller.UserController","classTag":"用户管理","methodName":"register","methodTag":"注册","methodDetail":"com.knife.example.controller.UserController.register(com.knife.example.bo.UserBO)","lineNumber":null,"classNameAndLineNumber":null,"stackTrace":null,"appName":"","groupName":null,"clientIp":"12.34.5.6","callerIp":"12.34.5.6","hostIp":"12.34.5.6"}
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

备注：AOP内部已捕获异常，如果有问题，不会影响正常业务执行。

**3. 手动打印日志**

支持手动打印日志：
```
NiceLogUtil.createBuilder()
        .mark("创建订单")
        .info();
```

此工具支持通过链式构造参数并打印，每次输入.都会有代码提示y。

**4. 手动收集日志**

在类或者方法上加@NiceLog，即可收集出入参、返回值、异常信息。

**5. 注解大全**

| 注解  | 使用位置  | 作用  | 示例 |
| ------------ | ------------ | ------------ | ------------ |
| @NiceLog | 类/方法 | 自动收集某个类/方法的日志。如果配置了suchtool.nicelog.collect-all为false，可以使用此注解单独收集日志。  | @NiceLog(value = "用户注册", businessNoSpEL = "#userBO.username") |
| @NiceLogIgnore  | 类/方法 | 不自动收集此类/方法的日志  | @NiceLogIgnore |

@NiceLog字段含义：

| 字段       | 描述                  | 默认值  |
|-----------|-----------------------|------|
| value | 代表当前类或方法，用于classTag、methodTag字段。 | 空字符串 |
| businessNoSpEL | 指定业务单号（SpEL），用于businessNo字段。如果参数是对象，这样写：#对象名.字段名，例如：#user.userName；如果参数不是对象，这样写：#字段名。例如：#orderNo | 空字符串 |

@NiceLogOperation字段含义：

此注解无字段。

## 5.详细配置

### 5.1 yml配置

支持SpringBoot的配置文件进行配置，比如：application.yml。

| 配置                                              | 描述                                 | 默认值  |
|-------------------------------------------------|------------------------------------|------|
| suchtool.nicelog.enabled                        | 启用日志                               | true |
| suchtool.nicelog.log-level                      | 日志收集级别。支持：debug、info、warn、error | info |
| suchtool.nicelog.stack-trace-package-name       | 收集栈日志的包名（前缀）。为空则全部收集       | 空 |
| suchtool.nicelog.auto-collect               | 自动收集日志（Controller、XXL-JOB等） | true |
| suchtool.nicelog.auto-collect-package-name      | 自动收集的包名（前缀）。为空则全部收集 | 空 |
| suchtool.nicelog.enable-controller-log          | 启用Controller日志                     | true |
| suchtool.nicelog.enable-xxl-job-log             | 启用XXL-JOB日志                        | true |
| suchtool.nicelog.enable-scheduled-log           | 启用@Scheduled日志                     | true |
| suchtool.nicelog.enable-nice-log-annotation-log | 启用@NiceLog日志                       | true |
| suchtool.nicelog.enable-rabbit-mq-log           | 启用RabbitMQ日志                       | true |
| suchtool.nicelog.enable-rocket-mq-log           | 启用RocketMQ日志                       | true |
| suchtool.nicelog.enable-kafka-log               | 启用Kafka日志                          | true |
| suchtool.nicelog.enable-feign-log               | 启用Feign日志                          | true |
| suchtool.nicelog.ignore-feign-log-package-name  | 不收集Feign日志的包名（前缀）。为空则全部收集 | 空  |
| suchtool.nicelog.feign-trace-id-header          | feign的traceId的header名字             | nice-log-trace-id |


### 5.2 设置优先级

**NiceLog的优先级**

NiceLog是通过AOP实现的，可以指定本组件的执行顺序，在SpringBoot的启动类上加如下注解即可：
```
@EnableNiceLog(order = 1)
```
比如：
```
package com.knife.example;

import com.suchtool.nicelog.annotation.EnableNiceLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableNiceLog(order = 1)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```

**具体的优先级**

可以用SpringBoot的配置文件指定它们的优先级，优先级越低，则执行顺序越靠前。

比如：一个Controller上，同时有@NiceLog，@NiceLog数值大，则调用Controller的日志记录逻辑。

默认情况下，Controller等原来的注解优先级最高，NiceLog注解优先级最低。

```yaml
suchtool:
  nicelog:
    controller-log-order: 10000  # Controller接口日志的顺序。默认值: 10000
    xxl-job-log-order: 10001  # XxlJob日志的顺序。默认值: 10001
    rabbit-mq-log-order: 10002  # RabbitMQ日志的顺序。默认值: 10002
    rocket-mq-log-order: 10003  # RocketMQ日志的顺序。默认值: 10003
    kafka-log-order: 10004  # Kafka日志的顺序。默认值: 10004
    feign-log-order: 10005  # Feign日志的顺序。默认值: 10005
    feign-request-interceptor-order: 10006  # Feign请求拦截器的顺序。默认值: 10006
    scheduled-log-order: 10007  # Scheduled日志的顺序。默认值: 10007
    nice-log-annotation-log-order: 10008  # NiceLog注解日志的顺序。默认值: 10008
```

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
| Key                    | 含义             | 备注                                                                                                                                                                                   |
|------------------------|----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| traceId                | 链路id           | 作为上下文传递                                                                                                                                                                              |
| mark                   | 标记             | 手动时可自定义                                                                                                                                                                              |
| logTime                | 日志时间          |                                                                                                                                                                                      |
| level                  | 级别             | DEBUG、INFO、WARN、ERROR                                                                                                                                                                |
| directionType          | 方向             | IN：方法进入；OUT：方法退出；INNER：方法内部执行                                                                                                                                                        |
| businessNo             | 业务单号         | 手动时可自定义                                                                                                                                                                              |
| message                | 信息             | 手动时可自定义                                                                                                                                                                              |
| errorInfo              | 错误信息         | 手动时可自定义                                                                                                                                                                              |
| errorDetailInfo        | 错误详细信息      | 手动时可自定义                                                                                                                                                                              |
| throwable              | Throwable异常类   | 手动时可自定义。栈追踪字符串会自动保存到NiceLogInnerBO.stackTrace                                                                                                                                        |
| recordStackTrace       | 记录栈追踪        | 手动时可自定义。用于非异常时主动获得栈追踪，会将栈追踪字符串会保存到NiceLogInnerBO.stackTrace。若throwable不为空，则使用throwable的栈数据                                                                                           |
| stackTrace             | 栈追踪字符串      |                                                                                                                                                                                      |
| entryType              | 入口类型         | MANUAL：手动；CONTROLLER：接口；RABBIT_MQ：RabbitMQ；XXL_JOB：XXL-JOB；NICE_LOG_ANNOTATION：NiceLog注解；FEIGN：Feign; ROCKETMQ：RocketMQ；KAFKA：Kafka |
| entry                  | 入口             | 对于Controller，是URL；对于RabbitMQ，是@RabbitListener的queues；对于XXL-JOB，是@XxlJob的value；对于Feign，是URL；对于RocketMQ，是@RocketMQMessageListener的topic字段；对于Kafka，是@KafkaListener的topics字段。作为上下文传递。    |
| entryClassTag          | 入口类的tag      | 取值优先级为：先取@NiceLog的value，若为空则取：对于Controller：Controller类上的@Api的tags > Controller类上的@Api的value；对于Feign：@FeignClient的value字段。作为上下文传递。                                                    |
| entryMethodTag         | 入口方法的tag    | 取值优先级为：@NiceLogOperation的value > @NiceLog的value > Controller方法上的@ApiOperation的value。作为上下文传递。                                                                                                                   |
| className              | 类名            |                                                                                                                                                                                      |
| classTag               | 当前类的tag      | 取值同entryClassTag，但不作为上下文传递。                                                                                                                                                          |
| methodName             | 方法名           |                                                                                                                                                                                      |
| methodTag              | 当前方法的tag    | 取值同entryMethodTag，但不作为上下文传递。                                                                                                                                                         |
| methodDetail           | 方法详情         | 全限定类名+方法名+全限定参数                                                                                                                                                                      |
| lineNumber             | 代码行号         | 只在手动输出时有值。                                                                                                                                                                           |
| classNameAndLineNumber | 类名及代码行号，中间用:隔开 | 只在手动输出时有值。                                                                                                                                                                           |
| param                  | 入参             | 手动时可自定义                                                                                                                                                                              |
| returnValue            | 返回值           | 手动时可自定义                                                                                                                                                                              |
| originReturnValue      | 原始返回值       | 手动时可自定义                                                                                                                                                                              |
| operatorId             | 操作人ID        | 手动时可自定义                                                                                                                                                                              |
| operatorName           | 操作人名字      | 手动时可自定义                                                                                                                                                                              |
| appName                | 应用名字          | 取spring.application.name配置                                                                                                                                                         |
| groupName              | 组名字           | 用于区分应用所在的组，建议放到公共组件里指定                                                                                                                                                               |
| clientIp               | 客户端IP          |                                                                                                                                                                                      |
| callerIp               | 调用方IP          |                                                                                                                                                                                      |
| hostIp                 | 主机IP          |                                                                                                                                                                                      |

## 7. 建表语句

如果想将日志存储到数据库或者ES，以下是推荐使用的建表语句。

### SQL
```sql 
CREATE TABLE `t_website_alarm_log` (
  `id` bigint NOT NULL COMMENT '主键',
  `trace_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '追踪ID',
  `mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标记',
  `log_time` datetime DEFAULT NULL COMMENT '日志时间',
  `level` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志级别',
  `direction_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '方向类型',
  `business_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务编号',
  `message` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息',
  `error_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '错误信息',
  `error_detail_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '错误详细信息',
  `stack_trace` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '堆栈跟踪',
  `entry_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '入口类型',
  `entry` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '入口',
  `entry_class_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '入口类标签',
  `entry_method_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '入口方法标签',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名称',
  `class_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类标签',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '方法名称',
  `method_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '方法标签',
  `method_detail` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '方法详细信息',
  `line_number` int DEFAULT NULL COMMENT '行号',
  `class_name_and_line_number` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名和行号',
  `param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '参数',
  `return_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '返回值',
  `origin_return_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '原始返回值',
  `operator_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人名字',
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用名称',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组名称',
  `client_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端IP',
  `caller_ip` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '调用方IP',
  `host_ip` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主机IP',
  `other1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他信息1',
  `other2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他信息2',
  `other3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他信息3',
  `other4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他信息4',
  `other5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他信息5',
  `other6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他信息6',
  `other7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他信息7',
  `other8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他信息8',
  `other9` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他信息9',
  `other10` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他信息10',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人ID',
  `create_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人名字',
  `update_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人ID',
  `update_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人名字',
  `delete_flag` bigint NOT NULL DEFAULT '0' COMMENT '删除标记。0：未删除；其他：已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE COMMENT '创建时间索引',
  KEY `idx_trace_id` (`trace_id`) USING BTREE COMMENT 'traceId索引',
  KEY `idx_entry_class_tag` (`entry_class_tag`) USING BTREE COMMENT '入口类标记',
  KEY `idx_entry_method_tag` (`entry_method_tag`) USING BTREE COMMENT '入口方法标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
``` 

### ES

待补充