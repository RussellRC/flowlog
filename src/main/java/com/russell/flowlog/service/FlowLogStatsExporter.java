package com.russell.flowlog.service;

import com.russell.flowlog.domain.PortProtocolPair;
import com.russell.flowlog.domain.Tag;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class FlowLogStatsExporter {

  @Nonnull private final FlowLogStats stats;

  @Inject
  public FlowLogStatsExporter(@Nonnull final FlowLogStats stats) {
    this.stats = stats;
  }

  public void exportFlowLogStats(@Nonnull final OutputStream out) {
    try (final var writer =
        new PrintWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)))) {
      writer.println("Tag Counts:");
      writer.println("Tag,Count");
      stats.getTagCounts().forEach((k, v) -> printTagCount(writer, k, v));
      writer.println();
      writer.println("Port/Protocol Combination Counts:");
      writer.println("Port,Protocol,Count");
      stats.getPortProtocolCounts().forEach((k, v) -> printPortProtoCount(writer, k, v));
    }
  }

  private static void printPortProtoCount(
      @Nonnull final PrintWriter writer,
      @Nonnull final PortProtocolPair pair,
      @Nonnull final Integer count) {
    writer.printf("%d,%s,%d%n", pair.getDstPort(), pair.getProtocol().getAcronym(), count);
  }

  private static void printTagCount(
      @Nonnull final PrintWriter writer, @Nonnull final Tag tag, @Nonnull final Integer count) {
    if (tag == Tag.UNKNOWN) {
      writer.printf("%s,%d%n", "Untagged", count);
    } else {
      writer.printf("%s,%d%n", tag.getTag(), count);
    }
  }
}
