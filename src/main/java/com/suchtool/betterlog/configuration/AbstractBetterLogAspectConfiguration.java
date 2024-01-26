package com.suchtool.betterlog.configuration;

import com.suchtool.betterlog.annotation.EnableBetterLog;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

@Configuration
public abstract class AbstractBetterLogAspectConfiguration implements ImportAware {
    @Nullable
    protected AnnotationAttributes enableBetterLog;

    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableBetterLog = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableBetterLog.class.getName(), false));
    }
}
