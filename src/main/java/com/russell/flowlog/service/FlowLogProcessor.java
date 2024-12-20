package com.russell.flowlog.service;

import com.google.common.annotations.VisibleForTesting;
import com.russell.flowlog.FlowLogDomainException;
import com.russell.flowlog.domain.FlowLogRecord;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FlowLogProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(FlowLogProcessor.class);

  @Nonnull private final ExecutorService executorService;
  @Nonnull private final FlowLogRecordMapper mapper;
  @Nonnull private final List<FlowLogRecordObserver> observers;

  @Inject
  public FlowLogProcessor(
      @Nonnull @Named("flowLogProcessorExecutorService") final ExecutorService executorService,
      @Nonnull final FlowLogRecordMapper mapper,
      @Nonnull final List<FlowLogRecordObserver> observers) {

    this.executorService = executorService;
    this.mapper = mapper;
    this.observers = observers;
  }

  /** Processes a flow log stream in a best-effort manner */
  public void processFlowLog(@Nonnull final InputStream in) {
    try (final BufferedReader reader =
        new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
      reader.lines().forEach(this::processLine);
    } catch (final IOException e) {
      throw new FlowLogDomainException("Unable to load tag mappings. " + e.getMessage(), e);
    }
  }

  @VisibleForTesting
  void processLine(@Nonnull final String line) {
    try {
      final var record = mapper.toRecord(line);
      observers.parallelStream()
          .map(observer -> createTask(observer, record))
          .forEach(executorService::submit);
    } catch (final LogRecordMappingException e) {
      LOG.warn("Unable to map record line {}", line, e);
    }
  }

  private static Runnable createTask(
      @Nonnull final FlowLogRecordObserver observer, @Nonnull final FlowLogRecord record) {
    return () -> observer.onRecordRead(record);
  }
}
