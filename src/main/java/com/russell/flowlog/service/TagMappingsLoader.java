package com.russell.flowlog.service;

import com.google.common.annotations.VisibleForTesting;
import com.russell.flowlog.FlowLogDomainException;
import com.russell.flowlog.domain.PortProtocolPair;
import com.russell.flowlog.domain.Protocol;
import com.russell.flowlog.domain.Tag;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Loads tag mappings from lines with the format: dstport,protocol,tag */
@Component
public class TagMappingsLoader {

  private static final Logger LOG = LoggerFactory.getLogger(TagMappingsLoader.class);

  private final TagMappingService service;

  @Inject
  public TagMappingsLoader(@Nonnull final TagMappingService service) {
    this.service = service;
  }

  public void loadMappings(@Nonnull final InputStream in) {
    try (final BufferedReader reader =
        new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
      reader.lines().forEach(this::loadMapping);
    } catch (final IOException e) {
      throw new FlowLogDomainException("Error while loading tag mappings: " + e.getMessage(), e);
    }
  }

  /** Load a tag mapping in a best-effort manner */
  @VisibleForTesting
  void loadMapping(final String line) {
    try {
      // Could've used regex "^.\\s*(\\d+)\\s*,\\s*(\\w+)\\s*,\\s*(\\w+)\\s*$"
      // or a CSV reading library, but seemed like an overkill
      final var split = line.split(",");
      if (split.length == 3) {
        final int dstPort = Integer.parseInt(split[0].trim());
        final String protocol = split[1].trim();
        final String tag = split[2].trim();
        service.putMapping(
            PortProtocolPair.of(dstPort, Protocol.fromString(protocol)), Tag.of(tag));
      }
    } catch (final Exception e) {
      LOG.warn("Unable to load tag mapping: {}", line);
    }
  }
}
