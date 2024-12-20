package com.russell.flowlog.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.russell.flowlog.domain.PortProtocolPair;
import com.russell.flowlog.domain.Protocol;
import com.russell.flowlog.domain.Tag;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TagMappingsLoaderTest {

  private TagMappingService service;
  private TagMappingsLoader loader;

  @BeforeEach
  void beforeEach() {
    service = mock(TagMappingService.class);
    loader = new TagMappingsLoader(service);
  }

  @Test
  void loadMappings() throws IOException {
    try (final InputStream in = getClass().getResourceAsStream("tag_mappings.csv")) {
      assertNotNull(in);
      loader.loadMappings(in);
      verify(service, times(11)).putMapping(any(), any());
    }
  }

  @Test
  void loadMapping() {
    loader.loadMapping("25,tcp,sv_P1");
    verify(service, times(1))
        .putMapping(PortProtocolPair.of(25, Protocol.fromString("tcp")), Tag.of("sv_P1"));
  }

  @Test
  void loadMapping_withInvalidLine_shouldNotFail() {
    loader.loadMapping("not-a-number,tcp,sv_P1");
    verifyNoInteractions(service);
  }
}
