package com.russell.flowlog.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.russell.flowlog.domain.FlowLogRecord;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlowLogProcessorTest {

  private FlowLogRecordMapper mapper;
  private FlowLogRecordObserver observer;
  private FlowLogProcessor processor;
  private ExecutorService executorService;
  private FlowLogRecord record;

  @BeforeEach
  public void beforeEach() throws LogRecordMappingException {
    record = mock(FlowLogRecord.class);
    mapper = mock(FlowLogRecordMapper.class);
    when(mapper.toRecord(any())).thenReturn(record);

    observer = mock(FlowLogRecordObserver.class);
    executorService = Executors.newSingleThreadExecutor();
    processor = new FlowLogProcessor(executorService, mapper, List.of(observer));
  }

  @AfterEach
  public void afterEach() {
    executorService.shutdown();
  }

  @Test
  void processLine() throws LogRecordMappingException {
    final var line = "";
    processor.processLine(line);

    verify(mapper, times(1)).toRecord(line);
    verify(observer, times(1)).onRecordRead(record);
  }

  @Test
  void processFlowLog() throws IOException, LogRecordMappingException {
    try (final InputStream in = getClass().getResourceAsStream("flow_log.txt")) {
      assertNotNull(in);
      processor.processFlowLog(in);
      verify(mapper, times(14)).toRecord(any());
      verify(observer, times(14)).onRecordRead(record);
    }
  }
}
