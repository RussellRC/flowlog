package com.russell.flowlog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.russell.flowlog.domain.FlowLogRecord;
import com.russell.flowlog.domain.PortProtocolPair;
import com.russell.flowlog.domain.Protocol;
import com.russell.flowlog.domain.Tag;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FlowLogStatsTest {

  private static final Tag TAG_SVP1 = Tag.of("sv_P1");
  private static final Tag TAG_SVP2 = Tag.of("sv_P2");
  private static final PortProtocolPair PP_25_6 = PortProtocolPair.of(25, Protocol.fromPort(6));
  private static final PortProtocolPair PP_68_17 = PortProtocolPair.of(68, Protocol.fromPort(17));

  private TagMappingService service;
  private FlowLogStatsImpl stats;

  @BeforeEach
  void beforeEach() {
    service = mock(TagMappingService.class);
    when(service.getTag(PP_25_6)).thenReturn(Optional.of(TAG_SVP1));
    when(service.getTag(PP_68_17)).thenReturn(Optional.of(TAG_SVP2));

    stats = spy(new FlowLogStatsImpl(service));
  }

  @Test
  void onRecordRead() {
    // GIVEN a record with dstport 25 and protocol 6 (tcp)
    final var record = mock(FlowLogRecord.class);
    when(record.dstPort()).thenReturn(25);
    when(record.protocol()).thenReturn(6);

    // WHEN onRecordRead is called
    stats.onRecordRead(record);

    // THEN tagService should return expected tag
    verify(service, times(1)).getTag(PP_25_6);

    // AND counters should be incremented
    verify(stats, times(1)).incrementPortProtocolPairCount(PP_25_6);
    verify(stats, times(1)).incrementTagCount(TAG_SVP1);
  }

  @Test
  void onRecordRead_unknownTagMapping() {
    // GIVEN an empty tag mapping
    final var record = mock(FlowLogRecord.class);
    when(record.dstPort()).thenReturn(25);
    when(record.protocol()).thenReturn(6);
    when(service.getTag(any())).thenReturn(Optional.empty());

    // WHEN onRecordRead is called
    stats.onRecordRead(record);

    verify(service, times(1)).getTag(PP_25_6);

    // Port-protocol pair counters must be incremented
    verify(stats, times(1)).incrementPortProtocolPairCount(PP_25_6);

    // Tag counter must be incremented for Unknown Tag
    verify(stats, times(1)).incrementTagCount(Tag.UNKNOWN);
  }

  @Test
  void incrementPortProtocolPairCount() {
    stats.incrementPortProtocolPairCount(PP_25_6);
    stats.incrementPortProtocolPairCount(PP_25_6);
    stats.incrementPortProtocolPairCount(PP_68_17);

    final var portProtocolCounts = stats.getPortProtocolCounts();
    assertEquals(2, portProtocolCounts.get(PP_25_6));
    assertEquals(1, portProtocolCounts.get(PP_68_17));
  }

  @Test
  void incrementTagCount() {
    stats.incrementTagCount(TAG_SVP2);
    stats.incrementTagCount(TAG_SVP1);
    stats.incrementTagCount(TAG_SVP2);

    final var tagCounts = stats.getTagCounts();
    assertEquals(2, tagCounts.get(TAG_SVP2));
    assertEquals(1, tagCounts.get(TAG_SVP1));
  }
}
