package com.russell.flowlog.service;

import com.russell.flowlog.domain.PortProtocolPair;
import com.russell.flowlog.domain.Tag;
import java.util.Map;
import javax.annotation.Nonnull;

/** Contract for statistics service regarding Flow Log Records */
public interface FlowLogStats {

  Map<Tag, Integer> getTagCounts();

  Map<PortProtocolPair, Integer> getPortProtocolCounts();

  void incrementPortProtocolPairCount(@Nonnull PortProtocolPair portProtoPair);

  void incrementTagCount(@Nonnull Tag tag);
}
