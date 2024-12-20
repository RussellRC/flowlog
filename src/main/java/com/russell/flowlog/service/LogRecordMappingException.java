package com.russell.flowlog.service;

/**
 * Exception thrown when an error occurs while mapping a {@link
 * com.russell.flowlog.domain.FlowLogRecord}
 */
public class LogRecordMappingException extends Exception {

  public LogRecordMappingException(final String message) {
    super(message);
  }

  public LogRecordMappingException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
