package com.russell.flowlog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.russell.flowlog.domain.PortProtocolPair;
import com.russell.flowlog.domain.Protocol;
import com.russell.flowlog.domain.Tag;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TagMappingServiceTest {

  private TagMappingServiceImpl service;

  @BeforeEach
  void beforeEach() {
    service = new TagMappingServiceImpl(5);
  }

  @Test
  void putAndGetMappings() {
    final var pp1 = PortProtocolPair.of(1, Protocol.fromString("tcp"));
    final var pp2 = PortProtocolPair.of(2, Protocol.fromString("tcp"));
    final var pp3 = PortProtocolPair.of(3, Protocol.fromString("udp"));
    final var pp4 = PortProtocolPair.of(4, Protocol.fromString("udp"));

    final var tag1 = Tag.of("tag1");
    final var tag2 = Tag.of("tag2");

    service.putMapping(pp1, tag1);
    service.putMapping(pp2, tag1);
    service.putMapping(pp3, tag2);
    assertEquals(3, service.getMap().size());

    assertEquals(Optional.of(tag1), service.getTag(pp1));
    assertEquals(Optional.of(tag1), service.getTag(pp2));
    assertEquals(Optional.of(tag2), service.getTag(pp3));
    assertTrue(service.getTag(pp4).isEmpty());
  }
}
