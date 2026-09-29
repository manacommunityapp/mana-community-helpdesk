package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.AuditLogResponse;
import com.manacommunity.helpdesk.enums.AuditAction;
import com.manacommunity.helpdesk.model.HelpdeskAuditLog;
import com.manacommunity.helpdesk.repository.HelpdeskAuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final HelpdeskAuditLogRepository auditLogRepository;

    @Transactional
    public void record(Long communityId, Long ticketId, AuditAction action, Long actorUserId, String actorName, String details) {
        HelpdeskAuditLog logEntry = HelpdeskAuditLog.builder()
                .communityId(communityId)
                .ticketId(ticketId)
                .action(action)
                .actorUserId(actorUserId)
                .actorName(actorName)
                .details(details)
                .build();
        auditLogRepository.save(logEntry);
    }

    public Page<AuditLogResponse> getLogsByCommunity(Long communityId, Pageable pageable) {
        return auditLogRepository.findByCommunityIdOrderByCreatedAtDesc(communityId, pageable)
                .map(this::mapToResponse);
    }

    public List<AuditLogResponse> getLogsByTicket(Long ticketId) {
        return auditLogRepository.findByTicketIdOrderByCreatedAtDesc(ticketId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private AuditLogResponse mapToResponse(HelpdeskAuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .communityId(log.getCommunityId())
                .ticketId(log.getTicketId())
                .action(log.getAction())
                .actorUserId(log.getActorUserId())
                .actorName(log.getActorName())
                .details(log.getDetails())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
