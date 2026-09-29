package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.enums.TicketPriority;
import com.manacommunity.helpdesk.model.HelpdeskSlaPolicyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HelpdeskSlaPolicyConfigRepository extends JpaRepository<HelpdeskSlaPolicyConfig, Long> {
    List<HelpdeskSlaPolicyConfig> findByCommunityIdAndActiveTrue(Long communityId);
    Optional<HelpdeskSlaPolicyConfig> findByCommunityIdAndPriority(Long communityId, TicketPriority priority);
}
