package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.enums.AuditAction;
import com.manacommunity.helpdesk.model.HelpdeskAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpdeskAuditLogRepository extends JpaRepository<HelpdeskAuditLog, Long> {
    Page<HelpdeskAuditLog> findByCommunityIdOrderByCreatedAtDesc(Long communityId, Pageable pageable);
    List<HelpdeskAuditLog> findByTicketIdOrderByCreatedAtDesc(Long ticketId);
    Page<HelpdeskAuditLog> findByCommunityIdAndActionOrderByCreatedAtDesc(Long communityId, AuditAction action, Pageable pageable);
}
