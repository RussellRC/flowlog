package com.russell.flowlog.service;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.russell.flowlog.domain.PortProtocolPair;
import com.russell.flowlog.domain.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;

/**
 * In-memory implementation of {@link TagMappingService}.<br>
 * Each char in a Java string is 2 bytes, and assuming each entry in the table will be < 20 chars,
 * each entry will be 40 bytes.<br>
 * Even if each entry was ~1Kb, with a 10000 mappings file, storing the whole lookup table file in
 * memory would be ~10MB, which is quite OK for the purposes of this app. In a more memory-intensive
 * app, we might want to use a different storage technologie, like Redis.
 */
public class TagMappingServiceImpl implements TagMappingService {

  @Nonnull private final Map<PortProtocolPair, Tag> mappings;

  /**
   * Constructor
   *
   * @param initialCapacity the initial capacity of the mappings
   */
  public TagMappingServiceImpl(final int initialCapacity) {
    this.mappings = new HashMap<>(initialCapacity);
  }

  @Override
  public void putMapping(@Nonnull final PortProtocolPair key, @Nonnull final Tag tag) {
    mappings.put(key, tag);
  }

  @Override
  @Nonnull
  public Optional<Tag> getTag(@Nonnull final PortProtocolPair key) {
    return Optional.ofNullable(mappings.get(key));
  }

  @VisibleForTesting
  @Nonnull
  Map<PortProtocolPair, Tag> getMap() {
    return ImmutableMap.copyOf(this.mappings);
  }
}
