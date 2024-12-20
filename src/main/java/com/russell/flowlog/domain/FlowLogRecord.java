package com.russell.flowlog.domain;

import javax.annotation.Nonnull;

/**
 * Represents a flow log record, version 2 ONLY.
 * see: <a href="https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html">Flow Log Records</a>
 */
public record FlowLogRecord(
    int version,
    @Nonnull String accountId,
    @Nonnull String interfaceId,
    @Nonnull String srcAddr,
    @Nonnull String dstAddr,
    int srcPort,
    int dstPort,
    int protocol,
    long packets,
    long bytes,
    long start,
    long end,
    /* ACCEPT, REJECT */
    @Nonnull String action,
    /* OK, NODATA, SKIPDATA */
    @Nonnull String logStatus
    ) {

}
