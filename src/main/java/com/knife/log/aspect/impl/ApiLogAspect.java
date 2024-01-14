// package com.knife.log.aspect.impl;
//
// import com.knife.log.constant.LogLevelEnum;
// import com.knife.log.util.TraceIdUtil;
// import com.knife.log.util.log.bo.LogBO;
// import com.knife.log.util.log.context.LogContext;
// import com.knife.log.util.log.context.LogContextThreadLocal;
// import com.knife.log.util.log.inner.bo.LogInnerBO;
// import com.knife.log.util.log.inner.util.LogInnerUtil;
// import com.knife.util.JsonUtil;
// import com.knife.util.ThrowableUtil;
// import com.knife.util.constant.ProcessIgnoreUrl;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import org.aspectj.lang.JoinPoint;
// import org.aspectj.lang.annotation.*;
// import org.aspectj.lang.reflect.MethodSignature;
// import org.springframework.stereotype.Component;
// import org.springframework.util.Assert;
// import org.springframework.web.context.request.RequestContextHolder;
// import org.springframework.web.context.request.ServletRequestAttributes;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.lang.reflect.Method;
// import java.util.HashMap;
// import java.util.Map;
//
// /**
//  * Controller的日志
//  */
// @Aspect
// @Component
// public class ApiLogAspect {
//     @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
//     public void pointcut() {
//     }
//
//     @Before("pointcut()")
//     public void before(JoinPoint joinPoint) {
//         if (!requireProcess()) {
//             return;
//         }
//
//         MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//         Method method = methodSignature.getMethod();
//
//         String param = null;
//         try {
//             param = JsonUtil.toJson(assembleArgs(joinPoint.getArgs()));
//         } catch (Exception e) {
//             LogBO.builder()
//                     .exceptionInfo("JSON序列化异常")
//                     .exceptionDetail(ThrowableUtil.getStackTrace(e))
//                     .build()
//                     .error();
//
//             // 也可以这么写
//             // LogUtil.error(LogBO.builder()
//             //         .exceptionInfo("JSON序列化异常")
//             //         .exceptionDetail(ThrowableUtil.getStackTrace(e))
//             //         .build());
//         }
//
//         LogInnerBO logInnerBO = new LogInnerBO();
//         logInnerBO.setEntry(assembleUrl());
//         logInnerBO.setClassName(method.getDeclaringClass().getName());
//         logInnerBO.setClassTag(assembleClassTag(method));
//         logInnerBO.setMethodName(method.getName());
//         logInnerBO.setMethodTag(assembleMethodTag(method));
//         logInnerBO.setLevel(LogLevelEnum.INFO);
//         logInnerBO.setTypeDetail("接口请求");
//         logInnerBO.setParam(param);
//         // 这里可以填充header，本处省略
//         // innerBO.setHeader();
//
//         // 记录上下文信息
//         LogContext logContext = new LogContext();
//         logContext.setEntry(assembleUrl());
//         logContext.setTraceId(TraceIdUtil.createTraceId());
//         LogContextThreadLocal.write(logContext);
//
//         LogInnerUtil.record(logInnerBO);
//     }
//
//     @AfterReturning(value = "pointcut()", returning = "returnValue")
//     public void afterReturning(JoinPoint joinPoint, Object returnValue) {
//         if (!requireProcess()) {
//             return;
//         }
//
//         MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//         Method method = methodSignature.getMethod();
//
//         String response = JsonUtil.toJson(returnValue);
//
//         LogInnerBO logInnerBO = new LogInnerBO();
//         logInnerBO.setEntry(assembleUrl());
//         logInnerBO.setClassName(method.getDeclaringClass().getName());
//         logInnerBO.setClassTag(assembleClassTag(method));
//         logInnerBO.setMethodName(method.getName());
//         logInnerBO.setMethodTag(assembleMethodTag(method));
//         // 这里无法获得代码所在行
//         // logInnerBO.setCodeLineNumber(null);
//         logInnerBO.setLevel(LogLevelEnum.INFO);
//         logInnerBO.setTypeDetail("接口返回");
//         logInnerBO.setReturnValue(response);
//         // 这里可以填充header，本处省略
//         // innerBO.setHeader();
//
//         LogInnerUtil.record(logInnerBO);
//
//         returningOrThrowingProcess();
//     }
//
//     @AfterThrowing(value = "pointcut()", throwing = "throwingValue")
//     public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
//         if (!requireProcess()) {
//             return;
//         }
//
//         MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//         Method method = methodSignature.getMethod();
//
//         LogInnerBO logInnerBO = new LogInnerBO();
//         logInnerBO.setEntry(assembleUrl());
//         logInnerBO.setClassName(method.getDeclaringClass().getName());
//         logInnerBO.setClassTag(assembleClassTag(method));
//         logInnerBO.setMethodName(method.getName());
//         logInnerBO.setMethodTag(assembleMethodTag(method));
//         // 这里无法获得代码所在行
//         // logInnerBO.setCodeLineNumber(null);
//         logInnerBO.setLevel(LogLevelEnum.INFO);
//         logInnerBO.setTypeDetail("接口报错");
//         logInnerBO.setExceptionInfo(throwingValue.getMessage());
//         logInnerBO.setExceptionDetail(ThrowableUtil.getLastStackTrace(throwingValue, 5));
//         // 如果是存到ES，这里可以把所有栈信息都获取到
//         // logInnerBO.setExceptionDetail(ThrowableUtil.getStackTrace(throwingValue));
//
//         LogInnerUtil.record(logInnerBO);
//
//         returningOrThrowingProcess();
//     }
//
//     /**
//      * 判断是否需要处理
//      */
//     private boolean requireProcess() {
//         String url = assembleUrl();
//         return !ProcessIgnoreUrl.isInWrapperIgnoreUrl(url);
//     }
//
//     /**
//      * 正常返回或者抛异常的处理
//      */
//     private void returningOrThrowingProcess() {
//         ServletRequestAttributes servletRequestAttributes =
//                 (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
//         Assert.notNull(servletRequestAttributes, "RequestAttributes不能为null");
//         HttpServletResponse response = servletRequestAttributes.getResponse();
//
//         // 将traceId返给前端，这样即可通过traceId查到所有日志信息
//         response.addHeader("traceId", LogContextThreadLocal.read().getTraceId());
//
//         // 清除，防止内存泄露
//         LogContextThreadLocal.clear();
//     }
//
//     private String assembleClassTag(Method method) {
//         String classTag = null;
//
//         Class<?> declaringClass = method.getDeclaringClass();
//
//         if (declaringClass.isAnnotationPresent(Api.class)) {
//             Api api = declaringClass.getAnnotation(Api.class);
//             String[] tags = api.tags();
//             String value = api.value();
//             String tagJoin = String.join("+", tags);
//             if (tags.length > 0) {
//                 classTag = tagJoin;
//             } else {
//                 classTag = value;
//             }
//         }
//
//         return classTag;
//     }
//
//     private String assembleMethodTag(Method method) {
//         String methodTag = null;
//
//         if (method.isAnnotationPresent(ApiOperation.class)) {
//             methodTag = method.getAnnotation(ApiOperation.class).value();
//         }
//
//         return methodTag;
//     }
//
//     private String assembleUrl() {
//         String url = null;
//         ServletRequestAttributes servletRequestAttributes =
//                 (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
//         if (servletRequestAttributes != null) {
//             HttpServletRequest request = servletRequestAttributes.getRequest();
//             url = request.getRequestURI();
//         }
//
//         return url;
//     }
//
//     private Object assembleArgs(Object[] args) {
//         if (args == null) {
//             return null;
//         }
//
//         if (args.length == 1) {
//             return args[0];
//         }
//
//         Map<String, Object> map = new HashMap<>();
//         int i = 1;
//         for (Object arg : args) {
//             String key = "arg" + i;
//             map.put(key, arg);
//             i++;
//         }
//         return map;
//     }
// }