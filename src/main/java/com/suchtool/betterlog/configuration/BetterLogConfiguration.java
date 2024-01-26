package com.suchtool.betterlog.configuration;

import com.suchtool.betterlog.annotation.EnableBetterLog;
import com.suchtool.betterlog.aspect.impl.ControllerLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

@Configuration(proxyBeanMethods = false)
public class BetterLogConfiguration implements ImportAware {
    @Nullable
    protected AnnotationAttributes enableBetterLog;

    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableBetterLog = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableBetterLog.class.getName(), false));
    }

    @Bean(name = "com.suchtool.betterlog.controllerLogAspect")
    @ConditionalOnProperty(name = "com.suchtool.betterlog.controllerlog.enabled", havingValue = "true", matchIfMissing = true)
    public ControllerLogAspect controllerLogAspect() {
        Integer order = Ordered.LOWEST_PRECEDENCE;
        if (enableBetterLog != null) {
            order = enableBetterLog.<Integer>getNumber("controllerLogOrder");
        }

        return new ControllerLogAspect(order);
    }
}
