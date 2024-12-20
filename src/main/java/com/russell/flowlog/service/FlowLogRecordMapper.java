package com.russell.flowlog.service;

import com.google.common.annotations.VisibleForTesting;
import com.russell.flowlog.domain.FlowLogRecord;
import javax.annotation.Nonnull;

/** Maps a flow log record string to a {@link com.russell.flowlog.domain.FlowLogRecord} */
public class FlowLogRecordMapper {

  /**
   * Maps a String of a V2 Record Log line to a {@link FlowLogRecord} object
   * @param logLine string in Record Log line v2 format
   * @return a new FlowLogRecord object
   * @throws LogRecordMappingException if any error occurs parsing the line
   */
  public FlowLogRecord toRecord(@Nonnull final String logLine) throws LogRecordMappingException {
    final String[] split = logLine.split(" ");

    if (split.length != 14) {
      throw new LogRecordMappingException(
          String.format(
              "Expected 14 fields in record line version 2. Found %d instead.", split.length));
    }

    final var version = toInt(split, 0, "version");
    final var srcPort = toInt(split, 5, "srcPort");
    final var dstPort = toInt(split, 6, "dstPort");
    final var protocol = toInt(split, 7, "protocol");
    final var packets = toLong(split, 8, "packets");
    final var bytes = toLong(split, 9, "bytes");
    final var start = toLong(split, 10, "start");
    final var end = toLong(split, 11, "end");

    return new FlowLogRecord(
        version, split[1], split[2], split[3], split[4], srcPort, dstPort, protocol, packets, bytes,
        start, end, split[12], split[13]);
  }

  @VisibleForTesting
  static int toInt(@Nonnull final String[] logLine, final int position, final String fieldName)
      throws LogRecordMappingException {
    try {
      return Integer.parseInt(logLine[position]);
    } catch (final NumberFormatException e) {
      throw new LogRecordMappingException(
          String.format("Unable to parse field %s at position %s.", fieldName, position), e);
    }
  }

  @VisibleForTesting
  static long toLong(@Nonnull final String[] logLine, final int position, final String fieldName)
      throws LogRecordMappingException {
    try {
      return Long.parseLong(logLine[position]);
    } catch (final NumberFormatException e) {
      throw new LogRecordMappingException(
          String.format("Unable to parse field %s at position %s.", fieldName, position), e);
    }
  }
}
