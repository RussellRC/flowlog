package com.russell.flowlog.service;

import com.russell.flowlog.domain.FlowLogRecord;

import javax.annotation.Nonnull;

public interface FlowLogRecordObserver {

    void onRecordRead(@Nonnull final FlowLogRecord logRecord);
}
