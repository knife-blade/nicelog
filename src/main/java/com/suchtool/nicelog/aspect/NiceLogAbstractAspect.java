package com.suchtool.nicelog.aspect;

import com.suchtool.nicelog.annotation.NiceLog;
import com.suchtool.nicelog.annotation.NiceLogIgnore;
import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.property.NiceLogProperty;
import com.suchtool.nicetool.util.spring.ApplicationContextHolder;
import com.suchtool.nicetool.util.spring.SpringBootUtil;
import org.aspectj.lang.JoinPoint;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class NiceLogAbstractAspect {

    /**
     * 判断是否需要处理
     */
    public boolean requireProcess(Method method) {
        NiceLogProperty niceLogProperty = ApplicationContextHolder.getContext()
                .getBean(NiceLogProperty.class);

        Class<?> declaringClass = method.getDeclaringClass();

        // 如果不是收集所有，又不是@NiceLog注解的，则不处理
        if (!niceLogProperty.getAutoCollect()) {
            if (!method.isAnnotationPresent(NiceLog.class)
                    && !declaringClass.isAnnotationPresent(NiceLog.class)) {
                return false;
            }
        }

        // 如果指定了收集的包，若不匹配，则不收集
        List<String> autoCollectPackageNameList = niceLogProperty.getAutoCollectPackageName();
        if (CollectionUtils.isEmpty(autoCollectPackageNameList)) {
            autoCollectPackageNameList = new ArrayList<>();
            // 获得启动类
            String runPackageName = SpringBootUtil.parseRunClassPackage();
            autoCollectPackageNameList.add(runPackageName);
        }
        boolean requireCollect = false;
        for (String packageName : autoCollectPackageNameList) {
            if (declaringClass.getName().startsWith(packageName.trim())) {
                requireCollect = true;
                break;
            }
        }
        if (!requireCollect) {
            return false;
        }

        // 如果类或方法上有NiceLogIgnore，则不处理
        return !method.isAnnotationPresent(NiceLogIgnore.class)
                && !declaringClass.isAnnotationPresent(NiceLogIgnore.class);
    }

    public abstract String pointcutExpression();

    public abstract void before(JoinPoint joinPoint);

    public abstract void afterReturning(JoinPoint joinPoint, Object returnValue);

    public abstract void afterThrowing(JoinPoint joinPoint, Throwable throwingValue);

    /**
     * 正常返回或者抛异常的处理
     */
    public void returningOrThrowingProcess() {}

}
