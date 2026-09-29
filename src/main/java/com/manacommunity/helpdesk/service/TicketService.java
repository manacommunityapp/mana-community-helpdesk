package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.*;
import com.manacommunity.helpdesk.enums.*;
import com.manacommunity.helpdesk.model.*;
import com.manacommunity.helpdesk.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final HelpdeskTicketRepository ticketRepository;
    private final HelpdeskSubcategoryRepository subcategoryRepository;
    private final HelpdeskAssetRepository assetRepository;
    private final HelpdeskStatusHistoryRepository statusHistoryRepository;
    private final HelpdeskAttachmentRepository attachmentRepository;
    private final HelpdeskResolutionRepository resolutionRepository;
    private final HelpdeskVendorRepository vendorRepository;
    private final HelpdeskTechnicianRepository technicianRepository;

    private final ClassificationService classificationService;
    private final StatusTransitionService statusTransitionService;
    private final SlaService slaService;
    private final AssignmentService assignmentService;
    private final CommentService commentService;
    private final FeedbackService feedbackService;
    private final AuditService auditService;

    // ─── CREATE TICKET ────────────────────────────────────────────────────────

    @Transactional
    public TicketResponse createTicket(CreateTicketRequest req, Long userId, String userName,
                                       String userTower, String userFlat, Long communityId) {
        // 1. Natural-language classification
        ClassificationResult classification = classificationService.classify(req.getRawDescription(), communityId);

        HelpdeskCategory category = req.getCategory() != null ? req.getCategory() : classification.getCategory();
        TicketPriority priority = req.getPriority() != null ? req.getPriority() : classification.getPriority();
        ClassificationSource source = (req.getCategory() != null || req.getPriority() != null)
                ? ClassificationSource.ADMIN_OVERRIDE : classification.getSource();

        HelpdeskSubcategory subcategory = null;
        if (req.getSubcategoryId() != null) {
            subcategory = subcategoryRepository.findById(req.getSubcategoryId()).orElse(null);
        } else if (classification.getSubcategoryId() != null) {
            subcategory = subcategoryRepository.findById(classification.getSubcategoryId()).orElse(null);
        }

        HelpdeskAsset asset = null;
        if (req.getAssetId() != null) {
            asset = assetRepository.findById(req.getAssetId()).orElse(null);
        }

        String tower = req.getTower() != null ? req.getTower() : userTower;
        String flat = req.getFlatNumber() != null ? req.getFlatNumber() : userFlat;
        String title = req.getTitle() != null ? req.getTitle() : generateTitle(req.getRawDescription(), classification.getSubcategoryName());

        // 2. Generate unique Ticket Number (HD-YYYYMMDD-XXXX)
        String ticketNumber = generateTicketNumber();

        // 3. SLA Deadline
        LocalDateTime slaDeadline = slaService.calculateDeadline(communityId, priority);

        HelpdeskTicket ticket = HelpdeskTicket.builder()
                .ticketNumber(ticketNumber)
                .communityId(communityId)
                .reportedByUserId(userId)
                .reportedByName(userName)
                .flatNumber(flat)
                .tower(tower)
                .rawDescription(req.getRawDescription())
                .title(title)
                .category(category)
                .subcategory(subcategory)
                .towerLocation(req.getTowerLocation() != null ? req.getTowerLocation() : tower)
                .floorLocation(req.getFloorLocation())
                .areaLocation(req.getAreaLocation())
                .locationDescription(req.getLocationDescription())
                .asset(asset)
                .priority(priority)
                .classificationSource(source)
                .classificationConfidence(classification.getConfidence())
                .status(TicketStatus.NEW)
                .escalationLevel(EscalationLevel.NONE)
                .slaDeadline(slaDeadline)
                .emergencyFlag(classification.getEmergencyFlag())
                .build();

        HelpdeskTicket saved = ticketRepository.save(ticket);

        // Record initial status history & audit
        recordStatusHistory(saved, null, TicketStatus.NEW, userId, userName, "Ticket Created");
        auditService.record(communityId, saved.getId(), AuditAction.HELPDESK_TICKET_CREATED,
                userId, userName, "Ticket created: " + saved.getTitle() + " (" + priority + ")");

        // Initial attachments
        if (req.getAttachmentUrls() != null) {
            for (String url : req.getAttachmentUrls()) {
                HelpdeskAttachment att = HelpdeskAttachment.builder()
                        .ticketId(saved.getId())
                        .communityId(communityId)
                        .uploadedByUserId(userId)
                        .attachmentType(AttachmentType.BEFORE_PHOTO)
                        .fileName("Initial Photo")
                        .fileUrl(url)
                        .build();
                attachmentRepository.save(att);
            }
        }

        // 4. Auto Assignment
        assignmentService.autoAssign(saved);

        return mapToResponse(saved, true);
    }

    // ─── LIFECYCLE ACTIONS ───────────────────────────────────────────────────

    @Transactional
    public TicketResponse updateStatus(Long ticketId, UpdateTicketStatusRequest req,
                                       Long actorUserId, String actorName, Long communityId) {
        HelpdeskTicket ticket = getTicketEntity(ticketId, communityId);
        TicketStatus fromStatus = ticket.getStatus();
        TicketStatus toStatus = req.getNewStatus();

        statusTransitionService.validateTransition(fromStatus, toStatus);
        ticket.setStatus(toStatus);

        recordStatusHistory(ticket, fromStatus, toStatus, actorUserId, actorName, req.getReason());
        auditService.record(communityId, ticketId, mapStatusToAuditAction(toStatus),
                actorUserId, actorName, "Status changed from " + fromStatus + " to " + toStatus);

        return mapToResponse(ticketRepository.save(ticket), true);
    }

    @Transactional
    public TicketResponse assignTicket(Long ticketId, AssignTicketRequest req,
                                       Long actorUserId, String actorName, Long communityId) {
        HelpdeskTicket ticket = getTicketEntity(ticketId, communityId);
        assignmentService.manualAssign(ticket, req, actorUserId, actorName);
        recordStatusHistory(ticket, ticket.getStatus(), TicketStatus.ASSIGNED, actorUserId, actorName, req.getNotes());
        return mapToResponse(ticketRepository.save(ticket), true);
    }

    @Transactional
    public TicketResponse acknowledgeTicket(Long ticketId, Long actorUserId, String actorName, Long communityId) {
        HelpdeskTicket ticket = getTicketEntity(ticketId, communityId);
        statusTransitionService.validateTransition(ticket.getStatus(), TicketStatus.ACKNOWLEDGED);
        ticket.setStatus(TicketStatus.ACKNOWLEDGED);
        ticket.setAcknowledgedAt(LocalDateTime.now());
        recordStatusHistory(ticket, ticket.getStatus(), TicketStatus.ACKNOWLEDGED, actorUserId, actorName, "Acknowledged by technician/vendor");
        auditService.record(communityId, ticketId, AuditAction.HELPDESK_TICKET_ACKNOWLEDGED, actorUserId, actorName, "Ticket acknowledged");
        return mapToResponse(ticketRepository.save(ticket), true);
    }

    @Transactional
    public TicketResponse startWork(Long ticketId, Long actorUserId, String actorName, Long communityId) {
        HelpdeskTicket ticket = getTicketEntity(ticketId, communityId);
        statusTransitionService.validateTransition(ticket.getStatus(), TicketStatus.IN_PROGRESS);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setWorkStartedAt(LocalDateTime.now());
        recordStatusHistory(ticket, ticket.getStatus(), TicketStatus.IN_PROGRESS, actorUserId, actorName, "Work started on ticket");
        auditService.record(communityId, ticketId, AuditAction.HELPDESK_TICKET_STARTED, actorUserId, actorName, "Work started");
        return mapToResponse(ticketRepository.save(ticket), true);
    }

    @Transactional
    public TicketResponse resolveTicket(Long ticketId, ResolveTicketRequest req,
                                        Long actorUserId, String actorName, Long communityId) {
        HelpdeskTicket ticket = getTicketEntity(ticketId, communityId);
        statusTransitionService.validateTransition(ticket.getStatus(), TicketStatus.RESOLVED);

        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setResolvedAt(LocalDateTime.now());
        ticket.setResolutionNotes(req.getResolutionNotes());

        HelpdeskResolution resolution = HelpdeskResolution.builder()
                .ticketId(ticketId)
                .resolvedByUserId(actorUserId)
                .resolvedByName(actorName)
                .resolutionNotes(req.getResolutionNotes())
                .beforePhotoUrl(req.getBeforePhotoUrl())
                .afterPhotoUrl(req.getAfterPhotoUrl())
                .build();
        resolutionRepository.save(resolution);

        // Decrement technician workload & update vendor metrics
        if (ticket.getAssignedTechnicianId() != null) {
            technicianRepository.findById(ticket.getAssignedTechnicianId()).ifPresent(t -> {
                t.setCurrentWorkload(Math.max(0, t.getCurrentWorkload() - 1));
                technicianRepository.save(t);
            });
        }
        if (ticket.getAssignedVendorId() != null) {
            vendorRepository.findById(ticket.getAssignedVendorId()).ifPresent(v -> {
                v.setTotalTicketsCompleted(v.getTotalTicketsCompleted() + 1);
                vendorRepository.save(v);
            });
        }

        recordStatusHistory(ticket, ticket.getStatus(), TicketStatus.RESOLVED, actorUserId, actorName, req.getResolutionNotes());
        auditService.record(communityId, ticketId, AuditAction.HELPDESK_TICKET_RESOLVED, actorUserId, actorName, "Ticket marked as resolved");

        return mapToResponse(ticketRepository.save(ticket), true);
    }

    @Transactional
    public TicketResponse confirmResolution(Long ticketId, ConfirmResolutionRequest req,
                                            Long userId, String userName, Long communityId) {
        HelpdeskTicket ticket = getTicketEntity(ticketId, communityId);

        if (Boolean.TRUE.equals(req.getIsResolved())) {
            ticket.setStatus(TicketStatus.CLOSED);
            ticket.setClosedAt(LocalDateTime.now());
            ticket.setResolutionVerifiedByUserId(userId);
            recordStatusHistory(ticket, ticket.getStatus(), TicketStatus.CLOSED, userId, userName, "Resident confirmed resolution");
            auditService.record(communityId, ticketId, AuditAction.HELPDESK_TICKET_CLOSED, userId, userName, "Resolution confirmed and ticket closed");
        } else {
            ticket.setStatus(TicketStatus.REOPENED);
            ticket.setReopenCount(ticket.getReopenCount() + 1);
            if (ticket.getAssignedVendorId() != null) {
                vendorRepository.findById(ticket.getAssignedVendorId()).ifPresent(v -> {
                    v.setReopenedTickets(v.getReopenedTickets() + 1);
                    vendorRepository.save(v);
                });
            }
            recordStatusHistory(ticket, ticket.getStatus(), TicketStatus.REOPENED, userId, userName, "Reopened: " + req.getReopenReason());
            auditService.record(communityId, ticketId, AuditAction.HELPDESK_TICKET_REOPENED, userId, userName, "Reopened: " + req.getReopenReason());
        }

        return mapToResponse(ticketRepository.save(ticket), true);
    }

    @Transactional
    public TicketResponse cancelTicket(Long ticketId, String reason, Long userId, String userName, Long communityId) {
        HelpdeskTicket ticket = getTicketEntity(ticketId, communityId);
        statusTransitionService.validateTransition(ticket.getStatus(), TicketStatus.CANCELLED);
        ticket.setStatus(TicketStatus.CANCELLED);
        ticket.setCancelledAt(LocalDateTime.now());
        recordStatusHistory(ticket, ticket.getStatus(), TicketStatus.CANCELLED, userId, userName, "Cancelled: " + reason);
        auditService.record(communityId, ticketId, AuditAction.HELPDESK_TICKET_CANCELLED, userId, userName, "Ticket cancelled");
        return mapToResponse(ticketRepository.save(ticket), true);
    }

    // ─── QUERY METHODS ───────────────────────────────────────────────────────

    public Page<TicketSummaryResponse> getResidentTickets(Long userId, Long communityId, Pageable pageable) {
        return ticketRepository.findByCommunityIdAndReportedByUserId(communityId, userId, pageable)
                .map(this::mapSummary);
    }

    public Page<TicketSummaryResponse> getAdminTickets(Long communityId, Pageable pageable) {
        return ticketRepository.findByCommunityId(communityId, pageable)
                .map(this::mapSummary);
    }

    public Page<TicketSummaryResponse> getVendorTickets(Long vendorId, Long communityId, Pageable pageable) {
        return ticketRepository.findByCommunityIdAndAssignedVendorId(communityId, vendorId, pageable)
                .map(this::mapSummary);
    }

    public TicketResponse getTicketDetail(Long ticketId, Long communityId, boolean isStaffOrAdmin) {
        HelpdeskTicket ticket = getTicketEntity(ticketId, communityId);
        return mapToResponse(ticket, isStaffOrAdmin);
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    private HelpdeskTicket getTicketEntity(Long ticketId, Long communityId) {
        return ticketRepository.findById(ticketId)
                .filter(t -> t.getCommunityId().equals(communityId))
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));
    }

    private void recordStatusHistory(HelpdeskTicket ticket, TicketStatus from, TicketStatus to,
                                     Long actorUserId, String actorName, String reason) {
        HelpdeskStatusHistory history = HelpdeskStatusHistory.builder()
                .ticketId(ticket.getId())
                .communityId(ticket.getCommunityId())
                .fromStatus(from)
                .toStatus(to)
                .changedByUserId(actorUserId)
                .changedByName(actorName)
                .reason(reason)
                .build();
        statusHistoryRepository.save(history);
    }

    private String generateTicketNumber() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String randomSuffix = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "HD-" + dateStr + "-" + randomSuffix;
    }

    private String generateTitle(String rawDescription, String subcategory) {
        if (rawDescription == null) return "Helpdesk Request";
        String trimmed = rawDescription.trim();
        int firstDot = trimmed.indexOf('.');
        int firstNewLine = trimmed.indexOf('\n');
        int cutoff = Math.min(trimmed.length(), 60);
        if (firstDot > 0 && firstDot < cutoff) cutoff = firstDot;
        if (firstNewLine > 0 && firstNewLine < cutoff) cutoff = firstNewLine;
        return trimmed.substring(0, cutoff).trim();
    }

    private AuditAction mapStatusToAuditAction(TicketStatus status) {
        switch (status) {
            case CLASSIFIED: return AuditAction.HELPDESK_TICKET_CLASSIFIED;
            case ASSIGNED: return AuditAction.HELPDESK_TICKET_ASSIGNED;
            case ACKNOWLEDGED: return AuditAction.HELPDESK_TICKET_ACKNOWLEDGED;
            case IN_PROGRESS: return AuditAction.HELPDESK_TICKET_STARTED;
            case RESOLVED: return AuditAction.HELPDESK_TICKET_RESOLVED;
            case REOPENED: return AuditAction.HELPDESK_TICKET_REOPENED;
            case CLOSED: return AuditAction.HELPDESK_TICKET_CLOSED;
            case CANCELLED: return AuditAction.HELPDESK_TICKET_CANCELLED;
            default: return AuditAction.HELPDESK_TICKET_CREATED;
        }
    }

    public TicketSummaryResponse mapSummary(HelpdeskTicket t) {
        return TicketSummaryResponse.builder()
                .id(t.getId())
                .ticketNumber(t.getTicketNumber())
                .title(t.getTitle())
                .rawDescription(t.getRawDescription())
                .category(t.getCategory())
                .subcategoryName(t.getSubcategory() != null ? t.getSubcategory().getName() : null)
                .priority(t.getPriority())
                .status(t.getStatus())
                .tower(t.getTower())
                .flatNumber(t.getFlatNumber())
                .assignedTechnicianName(t.getAssignedTechnicianName())
                .slaDeadline(t.getSlaDeadline())
                .slaRemainingMinutes(slaService.getRemainingMinutes(t))
                .isSlaBreached(slaService.isBreached(t))
                .emergencyFlag(t.getEmergencyFlag())
                .createdAt(t.getCreatedAt())
                .build();
    }

    public TicketResponse mapToResponse(HelpdeskTicket t, boolean isStaffOrAdmin) {
        List<CommentResponse> comments = commentService.getComments(t.getId(), isStaffOrAdmin);
        List<AttachmentResponse> attachments = attachmentRepository.findByTicketIdOrderByCreatedAtAsc(t.getId())
                .stream().map(a -> AttachmentResponse.builder()
                        .id(a.getId())
                        .ticketId(a.getTicketId())
                        .uploadedByUserId(a.getUploadedByUserId())
                        .attachmentType(a.getAttachmentType())
                        .fileName(a.getFileName())
                        .fileUrl(a.getFileUrl())
                        .createdAt(a.getCreatedAt())
                        .build()).collect(Collectors.toList());

        FeedbackResponse feedback = feedbackService.getFeedback(t.getId());

        return TicketResponse.builder()
                .id(t.getId())
                .ticketNumber(t.getTicketNumber())
                .communityId(t.getCommunityId())
                .reportedByUserId(t.getReportedByUserId())
                .reportedByName(t.getReportedByName())
                .flatNumber(t.getFlatNumber())
                .tower(t.getTower())
                .rawDescription(t.getRawDescription())
                .title(t.getTitle())
                .category(t.getCategory())
                .subcategoryId(t.getSubcategory() != null ? t.getSubcategory().getId() : null)
                .subcategoryName(t.getSubcategory() != null ? t.getSubcategory().getName() : null)
                .towerLocation(t.getTowerLocation())
                .floorLocation(t.getFloorLocation())
                .areaLocation(t.getAreaLocation())
                .locationDescription(t.getLocationDescription())
                .assetId(t.getAsset() != null ? t.getAsset().getId() : null)
                .assetName(t.getAsset() != null ? t.getAsset().getName() : null)
                .assetCode(t.getAsset() != null ? t.getAsset().getAssetCode() : null)
                .priority(t.getPriority())
                .classificationSource(t.getClassificationSource())
                .classificationConfidence(t.getClassificationConfidence())
                .status(t.getStatus())
                .escalationLevel(t.getEscalationLevel())
                .assignedVendorId(t.getAssignedVendorId())
                .assignedVendorName(t.getAssignedVendorName())
                .assignedTechnicianId(t.getAssignedTechnicianId())
                .assignedTechnicianName(t.getAssignedTechnicianName())
                .assignedStaffUserId(t.getAssignedStaffUserId())
                .assignedStaffName(t.getAssignedStaffName())
                .slaDeadline(t.getSlaDeadline())
                .slaRemainingMinutes(slaService.getRemainingMinutes(t))
                .isSlaBreached(slaService.isBreached(t))
                .acknowledgedAt(t.getAcknowledgedAt())
                .workStartedAt(t.getWorkStartedAt())
                .resolvedAt(t.getResolvedAt())
                .closedAt(t.getClosedAt())
                .cancelledAt(t.getCancelledAt())
                .resolutionNotes(t.getResolutionNotes())
                .resolutionVerifiedByUserId(t.getResolutionVerifiedByUserId())
                .reopenCount(t.getReopenCount())
                .emergencyFlag(t.getEmergencyFlag())
                .comments(comments)
                .attachments(attachments)
                .feedback(feedback)
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}
