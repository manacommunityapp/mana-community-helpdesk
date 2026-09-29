package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.enums.AuditAction;
import com.manacommunity.helpdesk.enums.EscalationLevel;
import com.manacommunity.helpdesk.enums.TicketStatus;
import com.manacommunity.helpdesk.model.HelpdeskEscalationLog;
import com.manacommunity.helpdesk.model.HelpdeskTicket;
import com.manacommunity.helpdesk.repository.HelpdeskEscalationLogRepository;
import com.manacommunity.helpdesk.repository.HelpdeskTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlaWatchdogService {

    private final HelpdeskTicketRepository ticketRepository;
    private final HelpdeskEscalationLogRepository escalationLogRepository;
    private final SlaService slaService;
    private final AuditService auditService;

    private static final List<TicketStatus> TERMINAL_STATUSES = List.of(
            TicketStatus.RESOLVED,
            TicketStatus.RESIDENT_CONFIRMATION,
            TicketStatus.CLOSED,
            TicketStatus.CANCELLED
    );

    @Scheduled(fixedDelayString = "${helpdesk.sla.watchdog-interval-ms:60000}")
    @Transactional
    public void scanAndEscalateSla() {
        List<HelpdeskTicket> activeTickets = ticketRepository.findByStatusNotInAndSlaDeadlineIsNotNull(TERMINAL_STATUSES);

        for (HelpdeskTicket ticket : activeTickets) {
            double percent = slaService.getPercentConsumed(ticket);
            EscalationLevel current = ticket.getEscalationLevel();

            if (percent >= 100.0 && current != EscalationLevel.ADMIN_BREACHED) {
                // Breached -> escalate to Admin
                ticket.setEscalationLevel(EscalationLevel.ADMIN_BREACHED);
                logEscalation(ticket, EscalationLevel.ADMIN_BREACHED, "ADMIN",
                        "SLA breached! Ticket " + ticket.getTicketNumber() + " is overdue.");
                auditService.record(ticket.getCommunityId(), ticket.getId(), AuditAction.HELPDESK_SLA_BREACHED,
                        null, "SYSTEM_WATCHDOG", "SLA 100% breached at " + LocalDateTime.now());
                log.warn("SLA Breached for ticket {}", ticket.getTicketNumber());

            } else if (percent >= 90.0 && percent < 100.0 && current != EscalationLevel.SUPERVISOR_90_PERCENT && current != EscalationLevel.ADMIN_BREACHED) {
                // 90% consumed -> Supervisor escalation
                ticket.setEscalationLevel(EscalationLevel.SUPERVISOR_90_PERCENT);
                logEscalation(ticket, EscalationLevel.SUPERVISOR_90_PERCENT, "SUPERVISOR",
                        "Ticket " + ticket.getTicketNumber() + " has reached 90% SLA consumption.");
                auditService.record(ticket.getCommunityId(), ticket.getId(), AuditAction.HELPDESK_TICKET_ESCALATED,
                        null, "SYSTEM_WATCHDOG", "Escalated to Supervisor (90% SLA consumed)");
                log.info("SLA 90% reached for ticket {}", ticket.getTicketNumber());

            } else if (percent >= 75.0 && percent < 90.0 && current == EscalationLevel.NONE) {
                // 75% consumed -> Staff reminder
                ticket.setEscalationLevel(EscalationLevel.STAFF_75_PERCENT);
                logEscalation(ticket, EscalationLevel.STAFF_75_PERCENT, "STAFF",
                        "Ticket " + ticket.getTicketNumber() + " has reached 75% SLA consumption.");
                auditService.record(ticket.getCommunityId(), ticket.getId(), AuditAction.HELPDESK_TICKET_ESCALATED,
                        null, "SYSTEM_WATCHDOG", "Escalated to Assigned Staff (75% SLA consumed)");
                log.info("SLA 75% reached for ticket {}", ticket.getTicketNumber());
            }

            ticketRepository.save(ticket);
        }
    }

    private void logEscalation(HelpdeskTicket ticket, EscalationLevel level, String role, String message) {
        HelpdeskEscalationLog logEntry = HelpdeskEscalationLog.builder()
                .ticketId(ticket.getId())
                .communityId(ticket.getCommunityId())
                .escalationLevel(level)
                .notifiedRole(role)
                .message(message)
                .build();
        escalationLogRepository.save(logEntry);
    }
}
