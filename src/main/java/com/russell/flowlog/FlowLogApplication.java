package com.russell.flowlog;

import com.russell.flowlog.service.FlowLogRecordMapper;
import com.russell.flowlog.service.FlowLogRecordObserver;
import com.russell.flowlog.service.FlowLogStatsImpl;
import com.russell.flowlog.service.TagMappingService;
import com.russell.flowlog.service.TagMappingServiceImpl;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nonnull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class FlowLogApplication {

  /**
   * Run just once
   * @param args mappings_file flow_log_file output_file
   */
  public static void main(String[] args) {
    System.exit(SpringApplication.exit(SpringApplication.run(FlowLogApplication.class, args)));
  }

  @Bean(name = "flowLogProcessorExecutorService")
  public ExecutorService executorService() {
    return Executors.newCachedThreadPool();
  }

  @Bean
  public FlowLogRecordMapper flowLogRecordMapper() {
    return new FlowLogRecordMapper();
  }

  @Bean
  public TagMappingService tagMappingService() {
    return new TagMappingServiceImpl(10000);
  }

  @Bean
  public FlowLogStatsImpl flowLogStats(@Nonnull final TagMappingService tagMappingService) {
    return new FlowLogStatsImpl(tagMappingService);
  }

  @Bean
  public List<FlowLogRecordObserver> flowLogRecordObservers(
      @Nonnull final FlowLogStatsImpl flowLogStats) {
    return List.of(flowLogStats);
  }
}
