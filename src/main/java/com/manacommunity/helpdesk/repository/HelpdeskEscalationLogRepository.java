package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.model.HelpdeskEscalationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpdeskEscalationLogRepository extends JpaRepository<HelpdeskEscalationLog, Long> {
    List<HelpdeskEscalationLog> findByTicketIdOrderByTriggeredAtAsc(Long ticketId);
    List<HelpdeskEscalationLog> findByCommunityIdOrderByTriggeredAtDesc(Long communityId);
}
