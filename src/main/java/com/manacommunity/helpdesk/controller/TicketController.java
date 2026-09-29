package com.manacommunity.helpdesk.controller;

import com.manacommunity.common.user.security.UserPrincipal;
import com.manacommunity.helpdesk.dto.*;
import com.manacommunity.helpdesk.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/helpdesk/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        Long userId = principal.getId();
        String name = principal.getAppUser().getFullName();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.createTicket(req, userId, name, req.getTower(), req.getFlatNumber(), communityId));
    }

    @GetMapping
    public ResponseEntity<Page<TicketSummaryResponse>> getResidentTickets(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 20) Pageable pageable) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(ticketService.getResidentTickets(principal.getId(), communityId, pageable));
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<TicketSummaryResponse>> getAdminTickets(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 20) Pageable pageable) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(ticketService.getAdminTickets(communityId, pageable));
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<Page<TicketSummaryResponse>> getVendorTickets(
            @PathVariable Long vendorId,
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 20) Pageable pageable) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(ticketService.getVendorTickets(vendorId, communityId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        boolean isStaffOrAdmin = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().contains("ADMIN") || a.getAuthority().contains("STAFF"));
        return ResponseEntity.ok(ticketService.getTicketDetail(id, communityId, isStaffOrAdmin));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketStatusRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(ticketService.updateStatus(id, req, principal.getId(), principal.getAppUser().getFullName(), communityId));
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<TicketResponse> assignTicket(
            @PathVariable Long id,
            @RequestBody AssignTicketRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(ticketService.assignTicket(id, req, principal.getId(), principal.getAppUser().getFullName(), communityId));
    }

    @PostMapping("/{id}/acknowledge")
    public ResponseEntity<TicketResponse> acknowledgeTicket(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(ticketService.acknowledgeTicket(id, principal.getId(), principal.getAppUser().getFullName(), communityId));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<TicketResponse> startWork(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(ticketService.startWork(id, principal.getId(), principal.getAppUser().getFullName(), communityId));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<TicketResponse> resolveTicket(
            @PathVariable Long id,
            @Valid @RequestBody ResolveTicketRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(ticketService.resolveTicket(id, req, principal.getId(), principal.getAppUser().getFullName(), communityId));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<TicketResponse> confirmResolution(
            @PathVariable Long id,
            @Valid @RequestBody ConfirmResolutionRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(ticketService.confirmResolution(id, req, principal.getId(), principal.getAppUser().getFullName(), communityId));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<TicketResponse> cancelTicket(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "Cancelled by user") String reason,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(ticketService.cancelTicket(id, reason, principal.getId(), principal.getAppUser().getFullName(), communityId));
    }
}
