package com.russell.flowlog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FlowLogRecordMapperTest {

  private FlowLogRecordMapper mapper;

  @BeforeEach
  void beforeEach() {
    mapper = new FlowLogRecordMapper();
  }

  @Test
  void toRecord() throws LogRecordMappingException {
    final var record =
        mapper.toRecord(
            "2 123456789012 eni-9h8g7f6e 172.16.0.100 203.0.113.102 110 49156 6 12 9000 1620140761 1620140821 ACCEPT OK");
    assertEquals(2, record.version());
    assertEquals("123456789012", record.accountId());
    assertEquals("eni-9h8g7f6e", record.interfaceId());
    assertEquals("172.16.0.100", record.srcAddr());
    assertEquals("203.0.113.102", record.dstAddr());
    assertEquals(110, record.srcPort());
    assertEquals(49156, record.dstPort());
    assertEquals(6, record.protocol());
    assertEquals(12, record.packets());
    assertEquals(9000, record.bytes());
    assertEquals(1620140761, record.start());
    assertEquals(1620140821, record.end());
    assertEquals("ACCEPT", record.action());
    assertEquals("OK", record.logStatus());
  }

  @Test
  void toRecord_moreThan14Fields_mustFail() {
    final var e =
        assertThrows(
            LogRecordMappingException.class,
            () ->
                mapper.toRecord(
                    "2 123456789012 eni-9h8g7f6e 172.16.0.100 203.0.113.102 110 49156 6 12 9000 1620140761 1620140821 ACCEPT OK vpc-id subnet-id"));
    assertEquals("Expected 14 fields in record line version 2. Found 16 instead.", e.getMessage());
  }

  @Test
  void toInt_badFormat_mustThrow() {
    // GIVEN a log line with a decimal number in the `version` field
    final var badLine =
        "3.1416 123456789012 eni-9h8g7f6e 172.16.0.100 203.0.113.102 110 49156 6 12 9000 1620140761 1620140821 ACCEPT OK";

    // WHEN mapping it to a record object
    final var e = assertThrows(LogRecordMappingException.class, () -> mapper.toRecord(badLine));

    // THEN it must fail
    assertEquals(NumberFormatException.class, e.getCause().getClass());
    assertEquals("Unable to parse field version at position 0.", e.getMessage());
  }

  @Test
  void toInt_outOfBounds_mustThrow() {
    // GIVEN a log line with a number bigger than an int in the `version` field
    final var bigNumber =
        new BigInteger(String.valueOf(Integer.MAX_VALUE)).add(new BigInteger("1"));
    final var badLine =
        String.format(
            "%s 123456789012 eni-9h8g7f6e 172.16.0.100 203.0.113.102 110 49156 6 12 9000 1620140761 1620140821 ACCEPT OK",
            bigNumber);

    // WHEN mapping it to a record object
    final var e = assertThrows(LogRecordMappingException.class, () -> mapper.toRecord(badLine));

    // THEN it must fail
    assertEquals(NumberFormatException.class, e.getCause().getClass());
    assertEquals("Unable to parse field version at position 0.", e.getMessage());
  }

  @Test
  void toLong_badFormat_mustThrow() {
    // GIVEN a log line with a decimal number in the `bytes` field
    final var badLine =
        "2 123456789012 eni-9h8g7f6e 172.16.0.100 203.0.113.102 110 49156 6 12 3.1416 1620140761 1620140821 ACCEPT OK";

    // WHEN mapping it to a record object
    final var e = assertThrows(LogRecordMappingException.class, () -> mapper.toRecord(badLine));

    // THEN it must fail
    assertEquals(NumberFormatException.class, e.getCause().getClass());
    assertEquals("Unable to parse field bytes at position 9.", e.getMessage());
  }

  @Test
  void toLong_outOfBounds_mustThrow() {
    // GIVEN a log line with a number bigger than a long in the `bytes` field
    final var bigNumber = new BigInteger(String.valueOf(Long.MAX_VALUE)).add(new BigInteger("1"));
    final var badLine =
        String.format(
            "2 123456789012 eni-9h8g7f6e 172.16.0.100 203.0.113.102 110 49156 6 12 %s 1620140761 1620140821 ACCEPT OK",
            bigNumber);

    // WHEN mapping it to a record object
    final var e = assertThrows(LogRecordMappingException.class, () -> mapper.toRecord(badLine));

    // THEN it must fail
    assertEquals(NumberFormatException.class, e.getCause().getClass());
    assertEquals("Unable to parse field bytes at position 9.", e.getMessage());
  }

  @Test
  void test() {
    System.out.println("49158PPPPPTTTTT".getBytes(StandardCharsets.UTF_8).length);
  }
}
