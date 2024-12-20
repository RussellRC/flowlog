package com.russell.flowlog;

import com.russell.flowlog.service.FlowLogProcessor;
import com.russell.flowlog.service.FlowLogStatsExporter;
import com.russell.flowlog.service.TagMappingsLoader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FlowLogRunner implements CommandLineRunner {

  final Logger LOG = LoggerFactory.getLogger(FlowLogRunner.class);

  @Nonnull private final FlowLogProcessor processor;
  @Nonnull private final TagMappingsLoader tagMappingsLoader;
  @Nonnull private final FlowLogStatsExporter statsExporter;

  @Inject
  public FlowLogRunner(
      @Nonnull final FlowLogProcessor processor,
      @Nonnull final TagMappingsLoader tagMappingsLoader,
      @Nonnull final FlowLogStatsExporter statsExporter) {

    this.processor = processor;
    this.tagMappingsLoader = tagMappingsLoader;
    this.statsExporter = statsExporter;
  }

  @Override
  public void run(String... args) throws Exception {
    if (args.length != 3) {
      System.out.println("Invalid arguments.");
      System.out.println("Usage: <mappings file> <flow logs file> <output file>");
      System.exit(-1);
    }

    LOG.info("Loading tag mappings file {}...", args[0]);
    try (final var mappings = new FileInputStream(args[0])) {
      tagMappingsLoader.loadMappings(mappings);
    }

    LOG.info("Processing flow log file {}...", args[1]);
    try (final var flowLogs = new FileInputStream(args[1])) {
      processor.processFlowLog(flowLogs);
    }

    LOG.info("Exporting statistics to file {}...", args[2]);
    try (final var out = new FileOutputStream(args[2])) {
      statsExporter.exportFlowLogStats(out);
    }
  }
}
