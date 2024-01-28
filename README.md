# nice-log

## 1.介绍

功能强大的Java日志组件。

## 2.功能
### 2.1 手动打印

手动打印日志

### 2.2 自动收集日志
在三处收集日志：进入时、返回时、报异常时。
默认情况下，只要你的项目里有相关的组件，比如XXL-JOB，就会自动收集其日志。
当前支持的组件有：

1. Controller日志
2. RabbitMQ日志
3. XXL-JOB日志

### 2.3 更多功能
准备支持：Feign日志、RocketMQ、Kafka等

## 3.快速开始

### 3.1 引入依赖
```xml
<dependency>
    <groupId>com.suchtool</groupId>
    <artifactId>nice-log-spring-boot-starter</artifactId>
    <version>{newest-version}</version>
</dependency>
```

### 3.2 查看日志
默认情况下，会自动收集相关日志，并通过logback输出。
支持手动处理日志：提供一个Bean，实现com.suchtool.nicelog.process.NiceLogProcess的void process(LogInnerBO logInnerBO)方法即可。

### 3.3 手动打印日志
支持手动打印日志：
```
LogBO.createBuilder()
        .mark("创建订单")
        .info();
```

此工具支持通过lambda的形式构造参数并打印，每次输入.都会有代码提示，类似于MyBatis-Plus的lambdaQuery


### 3.4 设置优先级
日志自动收集功能是通过AOP实现的，你可以手动指定AOP的优先级：在SpringBoot的启动类上加如下注解即可：
```
@EnableNiceLog(controllerLogOrder = 1, rabbitMQLogOrder = 2, xxlJobLogOrder = 3)
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
## 4.详细配置

| 配置  | 描述  | 默认值  |
| ------------ | ------------ | ------------ |
| suchtool.nicelog.enabled  | 启用日志  | true  |
| suchtool.nicelog.enableControllerLog  | 启用Controller日志  |  true |
| suchtool.nicelog.enableXxlJobLog  | 启用XXL-JOB日志  |  true |
| suchtool.nicelog.enableXxlJobLog  | 启用RabbitMQ日志  |  true |
