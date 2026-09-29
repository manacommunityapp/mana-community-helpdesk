package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.FeedbackRequest;
import com.manacommunity.helpdesk.dto.FeedbackResponse;
import com.manacommunity.helpdesk.enums.AuditAction;
import com.manacommunity.helpdesk.enums.TicketStatus;
import com.manacommunity.helpdesk.model.HelpdeskFeedback;
import com.manacommunity.helpdesk.model.HelpdeskTicket;
import com.manacommunity.helpdesk.model.HelpdeskVendor;
import com.manacommunity.helpdesk.repository.HelpdeskFeedbackRepository;
import com.manacommunity.helpdesk.repository.HelpdeskTicketRepository;
import com.manacommunity.helpdesk.repository.HelpdeskVendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final HelpdeskFeedbackRepository feedbackRepository;
    private final HelpdeskTicketRepository ticketRepository;
    private final HelpdeskVendorRepository vendorRepository;
    private final AuditService auditService;

    @Transactional
    public FeedbackResponse submitFeedback(Long ticketId, FeedbackRequest req, Long userId, Long communityId) {
        HelpdeskTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));

        if (ticket.getStatus() != TicketStatus.CLOSED && ticket.getStatus() != TicketStatus.RESOLVED
                && ticket.getStatus() != TicketStatus.RESIDENT_CONFIRMATION) {
            throw new IllegalStateException("Cannot submit feedback on ticket in status: " + ticket.getStatus());
        }

        if (feedbackRepository.findByTicketId(ticketId).isPresent()) {
            throw new IllegalStateException("Feedback already submitted for this ticket");
        }

        HelpdeskFeedback feedback = HelpdeskFeedback.builder()
                .ticketId(ticketId)
                .communityId(communityId)
                .userId(userId)
                .overallRating(req.getOverallRating())
                .resolutionQualityRating(req.getResolutionQualityRating())
                .responseTimeRating(req.getResponseTimeRating())
                .comment(req.getComment())
                .build();

        HelpdeskFeedback saved = feedbackRepository.save(feedback);

        // Update vendor scorecard average rating
        if (ticket.getAssignedVendorId() != null) {
            vendorRepository.findById(ticket.getAssignedVendorId()).ifPresent(vendor -> {
                double currentAvg = vendor.getAverageRating() != null ? vendor.getAverageRating() : 5.0;
                int total = vendor.getTotalTicketsCompleted() != null ? vendor.getTotalTicketsCompleted() : 1;
                double newAvg = ((currentAvg * total) + req.getOverallRating()) / (total + 1);
                vendor.setAverageRating(Math.round(newAvg * 10.0) / 10.0);
                vendorRepository.save(vendor);
            });
        }

        auditService.record(communityId, ticketId, AuditAction.HELPDESK_FEEDBACK_CREATED,
                userId, "RESIDENT", "Rating: " + req.getOverallRating() + "/5");

        return mapFeedback(saved);
    }

    public FeedbackResponse getFeedback(Long ticketId) {
        return feedbackRepository.findByTicketId(ticketId).map(this::mapFeedback).orElse(null);
    }

    public FeedbackResponse mapFeedback(HelpdeskFeedback f) {
        if (f == null) return null;
        return FeedbackResponse.builder()
                .id(f.getId())
                .ticketId(f.getTicketId())
                .communityId(f.getCommunityId())
                .userId(f.getUserId())
                .overallRating(f.getOverallRating())
                .resolutionQualityRating(f.getResolutionQualityRating())
                .responseTimeRating(f.getResponseTimeRating())
                .comment(f.getComment())
                .createdAt(f.getCreatedAt())
                .build();
    }
}
