package com.suchtool.nicelog.listener;

import com.suchtool.nicelog.process.impl.NiceLogProcessDefaultImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Slf4j
public class NiceLogEnvironmentChangeEventListener implements ApplicationListener<EnvironmentChangeEvent> {
    private final NiceLogProcessDefaultImpl niceLogProcessDefaultImpl;

    public NiceLogEnvironmentChangeEventListener(NiceLogProcessDefaultImpl niceLogProcessDefaultImpl) {
        this.niceLogProcessDefaultImpl = niceLogProcessDefaultImpl;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        try {
            Set<String> keys = event.getKeys();
            if (!CollectionUtils.isEmpty(keys)) {
                boolean requireUpdateConfig = false;
                for (String key : keys) {
                    if (key.startsWith("suchtool.nicelog.process")) {
                        requireUpdateConfig = true;
                        break;
                    }
                }

                if (requireUpdateConfig) {
                    niceLogProcessDefaultImpl.doCheckAndUpdateConfig();
                }
            }
        } catch (Exception e) {
            log.error("nicelog EnvironmentChangeEventListener error", e);
        }
    }
}
