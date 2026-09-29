package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.enums.TicketStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class StatusTransitionService {

    // Valid state transitions graph
    private static final Map<TicketStatus, Set<TicketStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(TicketStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(TicketStatus.NEW, Set.of(TicketStatus.CLASSIFIED, TicketStatus.ASSIGNED, TicketStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(TicketStatus.CLASSIFIED, Set.of(TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, TicketStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(TicketStatus.ASSIGNED, Set.of(TicketStatus.ACKNOWLEDGED, TicketStatus.IN_PROGRESS, TicketStatus.ASSIGNED, TicketStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(TicketStatus.ACKNOWLEDGED, Set.of(TicketStatus.IN_PROGRESS, TicketStatus.ASSIGNED, TicketStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(TicketStatus.IN_PROGRESS, Set.of(TicketStatus.WAITING_FOR_RESIDENT, TicketStatus.WAITING_FOR_VENDOR, TicketStatus.RESOLVED, TicketStatus.ASSIGNED, TicketStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(TicketStatus.WAITING_FOR_RESIDENT, Set.of(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED, TicketStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(TicketStatus.WAITING_FOR_VENDOR, Set.of(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED, TicketStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(TicketStatus.RESOLVED, Set.of(TicketStatus.RESIDENT_CONFIRMATION, TicketStatus.CLOSED, TicketStatus.REOPENED));
        ALLOWED_TRANSITIONS.put(TicketStatus.RESIDENT_CONFIRMATION, Set.of(TicketStatus.CLOSED, TicketStatus.REOPENED));
        ALLOWED_TRANSITIONS.put(TicketStatus.REOPENED, Set.of(TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, TicketStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(TicketStatus.CLOSED, Set.of(TicketStatus.REOPENED));
        ALLOWED_TRANSITIONS.put(TicketStatus.CANCELLED, Set.of());
    }

    public void validateTransition(TicketStatus currentStatus, TicketStatus newStatus) {
        if (currentStatus == newStatus) {
            return;
        }
        Set<TicketStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Set.of());
        if (!allowed.contains(newStatus)) {
            log.error("Invalid ticket transition from {} to {}", currentStatus, newStatus);
            throw new IllegalArgumentException(
                    String.format("Invalid status transition: cannot move ticket from '%s' to '%s'. Allowed: %s",
                            currentStatus, newStatus, allowed)
            );
        }
    }
}
