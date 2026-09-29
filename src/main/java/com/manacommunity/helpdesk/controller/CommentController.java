package com.manacommunity.helpdesk.controller;

import com.manacommunity.common.user.security.UserPrincipal;
import com.manacommunity.helpdesk.dto.CommentRequest;
import com.manacommunity.helpdesk.dto.CommentResponse;
import com.manacommunity.helpdesk.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helpdesk/tickets/{ticketId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long ticketId,
            @AuthenticationPrincipal UserPrincipal principal) {
        boolean isStaffOrAdmin = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().contains("ADMIN") || a.getAuthority().contains("STAFF"));
        return ResponseEntity.ok(commentService.getComments(ticketId, isStaffOrAdmin));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long ticketId,
            @Valid @RequestBody CommentRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        String role = principal.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(ticketId, communityId, req,
                        principal.getId(), principal.getAppUser().getFullName(), role));
    }
}
