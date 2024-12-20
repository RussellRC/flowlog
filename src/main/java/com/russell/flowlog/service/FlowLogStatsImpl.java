package com.russell.flowlog.service;

import com.google.common.collect.ImmutableMap;
import com.russell.flowlog.domain.FlowLogRecord;
import com.russell.flowlog.domain.PortProtocolPair;
import com.russell.flowlog.domain.Protocol;
import com.russell.flowlog.domain.Tag;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.inject.Inject;

/** Keeps track of statics related to {@link FlowLogRecord} objects */
public class FlowLogStatsImpl implements FlowLogStats, FlowLogRecordObserver {

  @Nonnull private final Map<Tag, AtomicInteger> tagCounts;

  @Nonnull private final Map<PortProtocolPair, AtomicInteger> portProtocolCounts;

  @Nonnull private final TagMappingService tagService;

  @Inject
  public FlowLogStatsImpl(@Nonnull final TagMappingService tagService) {
    this.tagCounts = new ConcurrentHashMap<>();
    this.portProtocolCounts = new ConcurrentHashMap<>();
    this.tagService = tagService;
  }

  @Override
  @Nonnull
  public Map<Tag, Integer> getTagCounts() {
    // This is inefficient and might not produce a consistent result,
    // but acceptable under assumption that it's not called too often, and freshness is not a hard
    // requirement
    return tagCounts.entrySet().stream()
        .map(e -> new SimpleEntry<>(e.getKey(), e.getValue().get()))
        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue));
  }

  @Override
  @Nonnull
  public Map<PortProtocolPair, Integer> getPortProtocolCounts() {
    // idem
    return portProtocolCounts.entrySet().stream()
        .map(e -> new SimpleEntry<>(e.getKey(), e.getValue().get()))
        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue));
  }

  @Override
  public void incrementPortProtocolPairCount(@Nonnull final PortProtocolPair portProtoPair) {
    portProtocolCounts.computeIfAbsent(portProtoPair, k -> new AtomicInteger(0)).getAndIncrement();
  }

  @Override
  public void incrementTagCount(@Nonnull final Tag tag) {
    tagCounts.computeIfAbsent(tag, t -> new AtomicInteger(0)).getAndIncrement();
  }

  @Override
  public void onRecordRead(@Nonnull final FlowLogRecord logRecord) {
    final PortProtocolPair portProtoPair =
        PortProtocolPair.of(logRecord.dstPort(), Protocol.fromPort(logRecord.protocol()));
    final Tag tag = tagService.getTag(portProtoPair).orElse(Tag.UNKNOWN);
    incrementPortProtocolPairCount(portProtoPair);
    incrementTagCount(tag);
  }
}
