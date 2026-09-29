package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.enums.TicketPriority;
import com.manacommunity.helpdesk.model.HelpdeskSlaPolicyConfig;
import com.manacommunity.helpdesk.model.HelpdeskTicket;
import com.manacommunity.helpdesk.repository.HelpdeskSlaPolicyConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlaService {

    private final HelpdeskSlaPolicyConfigRepository slaPolicyConfigRepository;

    public LocalDateTime calculateDeadline(Long communityId, TicketPriority priority) {
        int resolutionHours = getResolutionHours(communityId, priority);
        if (resolutionHours == 0) {
            // Immediate (Critical)
            return LocalDateTime.now().plusMinutes(30);
        }
        return LocalDateTime.now().plusHours(resolutionHours);
    }

    public int getResolutionHours(Long communityId, TicketPriority priority) {
        return slaPolicyConfigRepository.findByCommunityIdAndPriority(communityId, priority)
                .map(HelpdeskSlaPolicyConfig::getResolutionHours)
                .orElseGet(() -> defaultHoursForPriority(priority));
    }

    public Long getRemainingMinutes(HelpdeskTicket ticket) {
        if (ticket.getSlaDeadline() == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(ticket.getSlaDeadline())) {
            return -Duration.between(ticket.getSlaDeadline(), now).toMinutes();
        }
        return Duration.between(now, ticket.getSlaDeadline()).toMinutes();
    }

    public boolean isBreached(HelpdeskTicket ticket) {
        if (ticket.getSlaDeadline() == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(ticket.getSlaDeadline());
    }

    public double getPercentConsumed(HelpdeskTicket ticket) {
        if (ticket.getCreatedAt() == null || ticket.getSlaDeadline() == null) {
            return 0.0;
        }
        long totalDurationMinutes = Duration.between(ticket.getCreatedAt(), ticket.getSlaDeadline()).toMinutes();
        if (totalDurationMinutes <= 0) {
            return 100.0;
        }
        long elapsedMinutes = Duration.between(ticket.getCreatedAt(), LocalDateTime.now()).toMinutes();
        return Math.max(0.0, ((double) elapsedMinutes / totalDurationMinutes) * 100.0);
    }

    private int defaultHoursForPriority(TicketPriority priority) {
        switch (priority) {
            case CRITICAL: return 0; // Immediate (30 min)
            case HIGH: return 4;
            case MEDIUM: return 24;
            case LOW: return 48;
            default: return 24;
        }
    }
}
