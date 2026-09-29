package com.manacommunity.helpdesk.controller;

import com.manacommunity.common.user.security.UserPrincipal;
import com.manacommunity.helpdesk.dto.AuditLogResponse;
import com.manacommunity.helpdesk.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helpdesk/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    public ResponseEntity<Page<AuditLogResponse>> getCommunityAuditLogs(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 30) Pageable pageable) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(auditService.getLogsByCommunity(communityId, pageable));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<AuditLogResponse>> getTicketAuditLogs(
            @PathVariable Long ticketId) {
        return ResponseEntity.ok(auditService.getLogsByTicket(ticketId));
    }
}
