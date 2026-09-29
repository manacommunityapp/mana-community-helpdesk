package com.manacommunity.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long ticketId;
    private Long authorUserId;
    private String authorName;
    private String authorRole;
    private String message;
    private Boolean internalOnly;
    private LocalDateTime createdAt;
}
