package com.suchtool.nicelog.configuration;

import com.suchtool.nicelog.annotation.EnableNiceLog;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

@Configuration
public abstract class AbstractNiceLogAspectConfiguration implements ImportAware {
    @Nullable
    protected AnnotationAttributes enableNiceLog;

    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableNiceLog = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableNiceLog.class.getName(), false));
    }
}
