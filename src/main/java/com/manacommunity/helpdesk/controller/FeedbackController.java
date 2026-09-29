package com.manacommunity.helpdesk.controller;

import com.manacommunity.common.user.security.UserPrincipal;
import com.manacommunity.helpdesk.dto.FeedbackRequest;
import com.manacommunity.helpdesk.dto.FeedbackResponse;
import com.manacommunity.helpdesk.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/helpdesk/tickets/{ticketId}/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<FeedbackResponse> getFeedback(@PathVariable Long ticketId) {
        return ResponseEntity.ok(feedbackService.getFeedback(ticketId));
    }

    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(
            @PathVariable Long ticketId,
            @Valid @RequestBody FeedbackRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(feedbackService.submitFeedback(ticketId, req, principal.getId(), communityId));
    }
}
