package com.russell.flowlog.domain;

import java.util.Objects;
import javax.annotation.Nonnull;

/** Represents a unique (dstPort + protocol) pair */
public final class PortProtocolPair {
  private final int dstPort;
  private final Protocol protocol;

  /** Constructor */
  private PortProtocolPair(final int dstPort, @Nonnull final Protocol protocol) {
    this.dstPort = dstPort;
    this.protocol = protocol;
  }

  public int getDstPort() {
    return dstPort;
  }

  public Protocol getProtocol() {
    return protocol;
  }

  /** Static constructor method */
  public static PortProtocolPair of(final int dstPort, @Nonnull final Protocol protocol) {
    return new PortProtocolPair(dstPort, protocol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dstPort, protocol);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    PortProtocolPair lookupKey = (PortProtocolPair) o;
    return dstPort == lookupKey.dstPort && Objects.equals(protocol, lookupKey.protocol);
  }
}
