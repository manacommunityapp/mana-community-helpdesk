package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.AssignTicketRequest;
import com.manacommunity.helpdesk.enums.AuditAction;
import com.manacommunity.helpdesk.enums.TicketStatus;
import com.manacommunity.helpdesk.model.HelpdeskTechnician;
import com.manacommunity.helpdesk.model.HelpdeskTicket;
import com.manacommunity.helpdesk.model.HelpdeskVendor;
import com.manacommunity.helpdesk.repository.HelpdeskTechnicianRepository;
import com.manacommunity.helpdesk.repository.HelpdeskTicketRepository;
import com.manacommunity.helpdesk.repository.HelpdeskVendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentService {

    private final HelpdeskVendorRepository vendorRepository;
    private final HelpdeskTechnicianRepository technicianRepository;
    private final HelpdeskTicketRepository ticketRepository;
    private final AuditService auditService;

    @Transactional
    public void autoAssign(HelpdeskTicket ticket) {
        Long communityId = ticket.getCommunityId();
        if (ticket.getCategory() == null) {
            return;
        }

        String catName = ticket.getCategory().name();
        List<HelpdeskVendor> activeVendors = vendorRepository.findByCommunityIdAndActiveTrue(communityId);

        HelpdeskVendor matchedVendor = null;
        for (HelpdeskVendor v : activeVendors) {
            if (v.getSpecializations() != null && v.getSpecializations().toUpperCase().contains(catName)) {
                // If tower location matches service area or vendor serves all areas
                if (ticket.getTower() == null || v.getServiceAreas() == null || v.getServiceAreas().isBlank()
                        || v.getServiceAreas().toUpperCase().contains(ticket.getTower().toUpperCase())) {
                    matchedVendor = v;
                    break;
                }
            }
        }

        if (matchedVendor != null) {
            ticket.setAssignedVendorId(matchedVendor.getId());
            ticket.setAssignedVendorName(matchedVendor.getName());

            // Pick least-loaded technician under this vendor
            Optional<HelpdeskTechnician> techOpt = technicianRepository.findFirstByVendorIdAndActiveTrueOrderByCurrentWorkloadAsc(matchedVendor.getId());
            if (techOpt.isPresent()) {
                HelpdeskTechnician tech = techOpt.get();
                ticket.setAssignedTechnicianId(tech.getId());
                ticket.setAssignedTechnicianName(tech.getName());
                tech.setCurrentWorkload(tech.getCurrentWorkload() + 1);
                technicianRepository.save(tech);
            }

            ticket.setStatus(TicketStatus.ASSIGNED);
            auditService.record(communityId, ticket.getId(), AuditAction.HELPDESK_TICKET_ASSIGNED,
                    null, "SYSTEM_AUTO_ASSIGN", "Auto-assigned to vendor: " + matchedVendor.getName()
                            + (ticket.getAssignedTechnicianName() != null ? ", tech: " + ticket.getAssignedTechnicianName() : ""));

            log.info("Ticket {} auto-assigned to vendor {} and technician {}",
                    ticket.getTicketNumber(), ticket.getAssignedVendorName(), ticket.getAssignedTechnicianName());
        }
    }

    @Transactional
    public void manualAssign(HelpdeskTicket ticket, AssignTicketRequest req, Long actorUserId, String actorName) {
        if (req.getVendorId() != null) {
            HelpdeskVendor vendor = vendorRepository.findById(req.getVendorId())
                    .orElseThrow(() -> new IllegalArgumentException("Vendor not found: " + req.getVendorId()));
            ticket.setAssignedVendorId(vendor.getId());
            ticket.setAssignedVendorName(vendor.getName());
        }

        if (req.getTechnicianId() != null) {
            HelpdeskTechnician technician = technicianRepository.findById(req.getTechnicianId())
                    .orElseThrow(() -> new IllegalArgumentException("Technician not found: " + req.getTechnicianId()));
            ticket.setAssignedTechnicianId(technician.getId());
            ticket.setAssignedTechnicianName(technician.getName());
            technician.setCurrentWorkload(technician.getCurrentWorkload() + 1);
            technicianRepository.save(technician);
        }

        if (req.getStaffUserId() != null) {
            ticket.setAssignedStaffUserId(req.getStaffUserId());
        }

        ticket.setStatus(TicketStatus.ASSIGNED);
        auditService.record(ticket.getCommunityId(), ticket.getId(), AuditAction.HELPDESK_TICKET_ASSIGNED,
                actorUserId, actorName, "Assigned to vendor: " + ticket.getAssignedVendorName() + ", tech: " + ticket.getAssignedTechnicianName());
    }
}
