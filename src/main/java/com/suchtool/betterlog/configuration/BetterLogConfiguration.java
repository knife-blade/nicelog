package com.suchtool.betterlog.configuration;

import com.suchtool.betterlog.annotation.EnableBetterLog;
import com.suchtool.betterlog.aspect.impl.ControllerLogAspect;
import com.suchtool.betterlog.aspect.impl.XxlJobLogAspect;
import com.suchtool.betterlog.property.BetterLogProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

    /**
     * 只为代码提示，无实际作用
     */
    @Bean(name = "com.suchtool.betterlog.betterLogProperty")
    @ConfigurationProperties(prefix = "suchtool.betterlog")
    public BetterLogProperty betterLogProperty() {
        return new BetterLogProperty();
    }

    @Bean(name = "com.suchtool.betterlog.controllerLogAspect")
    @ConditionalOnProperty(name = "com.suchtool.betterlog.enableControllerLog", havingValue = "true", matchIfMissing = true)
    public ControllerLogAspect controllerLogAspect() {
        int order = Ordered.LOWEST_PRECEDENCE;
        if (enableBetterLog != null) {
            order = enableBetterLog.<Integer>getNumber("controllerLogOrder");
        }

        return new ControllerLogAspect(order);
    }

    @Bean(name = "com.suchtool.betterlog.xxlJobLogAspect")
    @ConditionalOnProperty(name = "com.suchtool.betterlog.enableXxlJobLog", havingValue = "true", matchIfMissing = true)
    public XxlJobLogAspect xxlJobLogAspect() {
        int order = Ordered.LOWEST_PRECEDENCE;
        if (enableBetterLog != null) {
            order = enableBetterLog.<Integer>getNumber("xxlJobLogOrder");
        }

        return new XxlJobLogAspect(order);
    }

}