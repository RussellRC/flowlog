package com.russell.flowlog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.russell.flowlog.domain.PortProtocolPair;
import com.russell.flowlog.domain.Protocol;
import com.russell.flowlog.domain.Tag;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FlowLogStatsExporterTest {

  private FlowLogStats flowLogStats;
  private FlowLogStatsExporter exporter;

  @BeforeEach
  void beforeEach() {
    flowLogStats = mock(FlowLogStats.class);
    exporter = new FlowLogStatsExporter(flowLogStats);
  }

  @Test
  void exportFlowLogStats() throws IOException {
    final var tagCounts = ImmutableMap.of(Tag.of("tag1"), 10, Tag.of("tag2"), 20, Tag.UNKNOWN, 5);
    when(flowLogStats.getTagCounts()).thenReturn(tagCounts);

    final var ppCounts =
        ImmutableMap.<PortProtocolPair, Integer>builder()
            .put(PortProtocolPair.of(6, Protocol.TCP), 66)
            .put(PortProtocolPair.of(6, Protocol.UDP), 666)
            .put(PortProtocolPair.of(7, Protocol.UDP), 77)
            .put(PortProtocolPair.of(8, Protocol.ICMP), 88)
            .build();
    when(flowLogStats.getPortProtocolCounts()).thenReturn(ppCounts);

    final var out = new ByteArrayOutputStream();
    exporter.exportFlowLogStats(out);

    final var result = out.toString(StandardCharsets.UTF_8);
    final var expected =
        Resources.toString(
            Resources.getResource(getClass(), "stats_export_gold1.txt"), StandardCharsets.UTF_8);
    assertEquals(expected, result);
  }
}
