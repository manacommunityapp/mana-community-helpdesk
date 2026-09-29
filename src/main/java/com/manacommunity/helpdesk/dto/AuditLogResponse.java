package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.AuditAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {
    private Long id;
    private Long communityId;
    private Long ticketId;
    private AuditAction action;
    private Long actorUserId;
    private String actorName;
    private String details;
    private LocalDateTime createdAt;
}
