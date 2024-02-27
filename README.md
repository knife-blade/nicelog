# nice-log

## 1.介绍

功能强大的Java日志组件。

## 2.功能

**1. 手动打印**

手动打印日志。

**2. 自动收集日志**

在三处收集日志：进入时、返回时、报异常时。

默认情况下，只要你的项目里有相关的组件，比如XXL-JOB，就会自动收集其日志。

当前支持的组件有：

1. Controller日志
2. RabbitMQ日志
3. XXL-JOB日志
4. Bean的方法或者类上加@NiceLog注解

**3. 更多功能**

准备支持：Feign日志、RocketMQ、Kafka等

## 3.快速开始

**1. 引入依赖**

```xml
<dependency>
    <groupId>com.suchtool</groupId>
    <artifactId>nice-log-spring-boot-starter</artifactId>
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
2024-01-31 21:43:07.230  INFO 25272 --- [nio-8080-exec-4] c.s.n.p.impl.NiceLogProcessDefaultImpl   : nicelog日志：{"param":"{\"user\":{\"id\":\"123\",\"loginCount\":null},\"email\":\"aa@qq.com\"}","returnValue":null,"mark":null,"errorInfo":null,"throwable":null,"other1":null,"other2":null,"other3":null,"other4":null,"other5":null,"appName":"","entry":"/test/test1","entryClassTag":"测试类","entryMethodTag":"测试1","className":"com.knife.example.controller.HelloController","classTag":"测试类","methodName":"test","methodTag":"测试1","methodDetail":"com.knife.example.controller.HelloController.test(com.knife.example.entity.User,java.lang.String)","codeLineNumber":null,"level":"INFO","aspectType":"CONTROLLER","directionType":"IN","traceId":"15a44a630dac4147993a0d1df44ae04e","logTime":"2024-01-31 21:43:07","clientIp":"10.0.10.110","ip":"10.0.10.110"}
2024-01-31 21:43:07.241  INFO 25272 --- [nio-8080-exec-4] c.s.n.p.impl.NiceLogProcessDefaultImpl   : nicelog日志：{"param":null,"returnValue":null,"mark":"我的打印","errorInfo":null,"throwable":null,"other1":null,"other2":null,"other3":null,"other4":null,"other5":null,"appName":"","entry":"/test/test1","entryClassTag":"测试类","entryMethodTag":"测试1","className":"com.knife.example.controller.HelloController","classTag":null,"methodName":"test","methodTag":null,"methodDetail":null,"codeLineNumber":"23","level":"INFO","aspectType":"MANUAL","directionType":null,"traceId":"15a44a630dac4147993a0d1df44ae04e","logTime":"2024-01-31 21:43:07","clientIp":null,"ip":null}
2024-01-31 21:43:07.242  INFO 25272 --- [nio-8080-exec-4] c.s.n.p.impl.NiceLogProcessDefaultImpl   : nicelog日志：{"param":null,"returnValue":"\"success\"","mark":null,"errorInfo":null,"throwable":null,"other1":null,"other2":null,"other3":null,"other4":null,"other5":null,"appName":"","entry":"/test/test1","entryClassTag":"测试类","entryMethodTag":"测试1","className":"com.knife.example.controller.HelloController","classTag":"测试类","methodName":"test","methodTag":"测试1","methodDetail":"com.knife.example.controller.HelloController.test(com.knife.example.entity.User,java.lang.String)","codeLineNumber":null,"level":"INFO","aspectType":"CONTROLLER","directionType":"OUT","traceId":"15a44a630dac4147993a0d1df44ae04e","logTime":"2024-01-31 21:43:07","clientIp":"10.0.10.110","ip":"10.0.10.110"}

```

## 4 使用说明

**1. 打印日志**

默认情况下，会通过logback输出。

支持自定义处理日志：提供一个Bean，实现com.suchtool.nicelog.process.NiceLogProcess的void process(NiceLogInnerBO niceLogInnerBO)方法即可。

**2. 自动收集日志**

自动收集相关组件的日志。

原理：除了Feign，都使用AOP。
Feign：实现RequestInterceptor接口收集请求日志，继承SpringDecoder收集响应日志。（若Feign用AOP，有些日志收集不到，比如：因响应格式不对导致的失败）


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

| 配置  | 描述  | 默认值  |
| ------------ | ------------ | ------------ |
| suchtool.nicelog.enabled  | 启用日志  | true  |
| suchtool.nicelog.collectAll  | 收集所有  | true  |
| suchtool.nicelog.enableControllerLog  | 启用Controller日志  |  true |
| suchtool.nicelog.enableXxlJobLog  | 启用XXL-JOB日志  |  true |
| suchtool.nicelog.enableRabbitMQLog  | 启用RabbitMQ日志  |  true |
| suchtool.nicelog.enableNiceLogAnnotationLog  | 启用RabbitMQ日志  |  true |
| suchtool.nicelog.enableFeignLog  | 启用Feign日志  |  true |

### 5.2 设置优先级
日志自动收集功能是通过AOP实现的（Feign不是用的AOP）。
你可以手动指定它们的优先级：在SpringBoot的启动类上加如下注解即可：
```
@EnableNiceLog(controllerLogOrder = 1, rabbitMQLogOrder = 2, xxlJobLogOrder = 3, niceLogAnnotationLogOrder = 4, feignRequestLogOrder = 5, feignResponseLogOrder = 6)
```
比如：
```
package com.knife.example;

import com.suchtool.nicelog.annotation.EnableNiceLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableNiceLog(controllerLogOrder = 1, rabbitMQLogOrder = 2, xxlJobLogOrder = 3)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```
## 6. 字段的含义
| Key            | 含义           | 备注                                                                                                   |
|----------------|--------------|------------------------------------------------------------------------------------------------------|
| param          | 入参           | 手动时可自定义                                                                                              |
| returnValue    | 返回值          | 手动时可自定义                                                                                              |
| mark           | 标记           | 手动时可自定义                                                                                              |
| errorInfo      | 错误信息         | 手动时可自定义                                                                                              |
| throwable      | Throwable异常类 | 手动时可自定义                                                                                              |
| appName        | 应用名字         | 取的是spring.application.name配置                                                                         |
| entryType      | 入口类型         | MANUAL：手动；CONTROLLER：接口；RABBIT_MQ：RabbitMQ；XXL_JOB：XXL-JOB；NICE_LOG_ANNOTATION：NiceLog注解；Feign：Feign |
| entry          | 入口           | 对于Controller，是URL；对于RabbitMQ，是@RabbitMQ的queues；对于XXL-JOB，是@XxlJob的value。作为上下文传递。                     |
| entryClassTag  | 入口类的tag      | 取值优先级为：@NiceLog的value > Controller类上的@Api的tags > Controller类上的@Api的value。作为上下文传递。                    |
| entryMethodTag | 入口方法的tag     | 取值优先级为：@NiceLog的value > Controller方法上的@ApiOperation的value。作为上下文传递。                                   |
| className      | 类名           |                                                                                                      |
| classTag       | 当前类的tag      | 取值同entryClassTag，但不作为上下文传递。|
| methodName     | 方法名          |  |
| methodTag      | 当前方法的tag     | 取值同entryMethodTag，但不作为上下文传递。                                                                         |
| methodDetail   | 方法详情         | 全限定类名+方法名+全限定参数                                                                                      |
| codeLineNumber | 代码所在的行数      | 只在手动输出时有值。                                                                                           |
| level          | 级别           | INFO、WARNING、ERROR                                                                                   |
| directionType  | 方向           | IN：方法进入；OUT：方法退出；INNER：方法内部执行                                                                        |
| traceId        | 链路id         | 作为上下文传递                                                                                              |
| logTime        | 日志时间         |                                                                                                      |
| clientIp       | 客户端IP        |                                                                                                      |
| ip             | 调用方IP        |                                                                                                      |
| other1         | 备用字段1        | 手动时可自定义                                                                                              |
| other2         | 备用字段2        | 手动时可自定义                                                                                              |
| other3         | 备用字段3        | 手动时可自定义                                                                                              |
| other4         | 备用字段4        | 手动时可自定义                                                                                              |
| other5         | 备用字段5        | 手动时可自定义                                                                                              |

