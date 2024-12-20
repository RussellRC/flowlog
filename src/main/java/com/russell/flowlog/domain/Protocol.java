package com.russell.flowlog.domain;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Locale;
import javax.annotation.Nonnull;

/**
 * IANA Protocols<br>
 * See: <a href="https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml">IANA</a>
 * See: <a href="https://en.wikipedia.org/wiki/List_of_IP_protocol_numbers">Wikipedia</a> NOTE:
 * NOTE: Mapped just a few for expediency
 */
public enum Protocol {
  Unknown(-1),
  HOTPOT(0),
  ICMP(1),
  IGMP(2),
  GGP(3),
  IPv4(4),
  ST(5),
  TCP(6),
  CBT(7),
  EGP(8),
  IGP(9),
  PUP(12),
  UDP(17),
  MUX(18),
  RDP(27),
  IRTP(28),
  DCCP(33),
  IDPR(35),
  XTP(36),
  DDP(37),
  IPv6(41),
  SDRP(42),
  RSVP(46),
  GRE(47),
  GMTP(48),
  STP(118),
  SMP(121),
  PTP(123),
  SCTP(132),
  Ethernet(143),
  ;

  static final BiMap<String, Protocol> acronyms =
      Arrays.stream(values())
          .map(e -> new SimpleEntry<>(e.name().toLowerCase(Locale.ROOT), e))
          .collect(ImmutableBiMap.toImmutableBiMap(SimpleEntry::getKey, SimpleEntry::getValue));

  static final BiMap<Integer, Protocol> portNumbers =
      Arrays.stream(values())
          .map(e -> new SimpleEntry<>(e.port, e))
          .collect(ImmutableBiMap.toImmutableBiMap(SimpleEntry::getKey, SimpleEntry::getValue));

  /** IANA port number */
  final int port;

  /** Constructor */
  Protocol(final int port) {
    this.port = port;
  }

  public int getPort() {
    return port;
  }

  public String getAcronym() {
    return acronyms.inverse().get(this);
  }

  public static Protocol fromString(@Nonnull final String acronym) {
    return acronyms.getOrDefault(acronym, Unknown);
  }

  public static Protocol fromPort(final int port) {
    return portNumbers.getOrDefault(port, Unknown);
  }
}
