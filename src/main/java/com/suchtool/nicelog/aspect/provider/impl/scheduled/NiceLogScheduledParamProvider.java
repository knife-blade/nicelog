package com.suchtool.nicelog.aspect.provider.impl.scheduled;

import com.suchtool.nicelog.aspect.provider.NiceLogParamProvider;
import com.suchtool.nicelog.constant.NiceLogEntryTypeEnum;
import com.suchtool.nicelog.property.NiceLogProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduled参数提供者
 */
@Slf4j
public class NiceLogScheduledParamProvider extends NiceLogParamProvider {
    public NiceLogScheduledParamProvider(NiceLogProperty niceLogProperty) {
        super(niceLogProperty);
    }

    @Override
    public String provideEntryType() {
        return NiceLogEntryTypeEnum.SCHEDULED.name();
    }
}