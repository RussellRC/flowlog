package com.russell.flowlog.service;

import com.russell.flowlog.domain.PortProtocolPair;
import com.russell.flowlog.domain.Tag;

import java.util.Optional;
import javax.annotation.Nonnull;

/** Service for (dstport,protocol -> tag) mappings. */
public interface TagMappingService {

  /** Add a LookupKey - Tag mapping */
  void putMapping(@Nonnull PortProtocolPair key, @Nonnull Tag tag);

  /**
   * Get the tag for a given key
   */
  @Nonnull
  Optional<Tag> getTag(@Nonnull PortProtocolPair key);
}
