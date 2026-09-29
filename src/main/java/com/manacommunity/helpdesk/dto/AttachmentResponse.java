package com.manacommunity.helpdesk.dto;

import com.manacommunity.helpdesk.enums.AttachmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentResponse {
    private Long id;
    private Long ticketId;
    private Long uploadedByUserId;
    private AttachmentType attachmentType;
    private String fileName;
    private String fileUrl;
    private LocalDateTime createdAt;
}
