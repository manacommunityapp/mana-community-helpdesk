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
public class FeedbackResponse {
    private Long id;
    private Long ticketId;
    private Long communityId;
    private Long userId;
    private Integer overallRating;
    private Integer resolutionQualityRating;
    private Integer responseTimeRating;
    private String comment;
    private LocalDateTime createdAt;
}
