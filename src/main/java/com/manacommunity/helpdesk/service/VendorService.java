package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.*;
import com.manacommunity.helpdesk.model.HelpdeskTechnician;
import com.manacommunity.helpdesk.model.HelpdeskVendor;
import com.manacommunity.helpdesk.model.HelpdeskVisit;
import com.manacommunity.helpdesk.repository.HelpdeskTechnicianRepository;
import com.manacommunity.helpdesk.repository.HelpdeskVendorRepository;
import com.manacommunity.helpdesk.repository.HelpdeskVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final HelpdeskVendorRepository vendorRepository;
    private final HelpdeskTechnicianRepository technicianRepository;
    private final HelpdeskVisitRepository visitRepository;

    public List<VendorResponse> getVendors(Long communityId) {
        return vendorRepository.findByCommunityId(communityId)
                .stream().map(this::mapVendor).collect(Collectors.toList());
    }

    @Transactional
    public VendorResponse createVendor(VendorRequest req, Long communityId) {
        HelpdeskVendor vendor = HelpdeskVendor.builder()
                .communityId(communityId)
                .name(req.getName())
                .contactPerson(req.getContactPerson())
                .phone(req.getPhone())
                .email(req.getEmail())
                .specializations(req.getSpecializations())
                .serviceAreas(req.getServiceAreas())
                .contractStartDate(req.getContractStartDate())
                .contractEndDate(req.getContractEndDate())
                .active(req.getActive() != null ? req.getActive() : true)
                .build();
        return mapVendor(vendorRepository.save(vendor));
    }

    public VendorScorecardResponse getScorecard(Long vendorId) {
        HelpdeskVendor v = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found: " + vendorId));
        return VendorScorecardResponse.builder()
                .vendorId(v.getId())
                .vendorName(v.getName())
                .ticketsCompleted(v.getTotalTicketsCompleted())
                .avgResponseMinutes(v.getAverageResponseMinutes())
                .avgResolutionMinutes(v.getAverageResolutionMinutes())
                .slaCompliancePercent(v.getSlaCompliancePercent())
                .residentRating(v.getAverageRating())
                .reopenedTickets(v.getReopenedTickets())
                .build();
    }

    // Technicians
    public List<TechnicianResponse> getTechnicians(Long vendorId) {
        return technicianRepository.findByVendorIdAndActiveTrue(vendorId)
                .stream().map(this::mapTechnician).collect(Collectors.toList());
    }

    @Transactional
    public TechnicianResponse createTechnician(TechnicianRequest req, Long communityId) {
        HelpdeskTechnician tech = HelpdeskTechnician.builder()
                .vendorId(req.getVendorId())
                .communityId(communityId)
                .name(req.getName())
                .phone(req.getPhone())
                .specialization(req.getSpecialization())
                .active(req.getActive() != null ? req.getActive() : true)
                .build();
        return mapTechnician(technicianRepository.save(tech));
    }

    // Visits
    public List<VisitResponse> getVisits(Long ticketId) {
        return visitRepository.findByTicketIdOrderByScheduledAtAsc(ticketId)
                .stream().map(this::mapVisit).collect(Collectors.toList());
    }

    @Transactional
    public VisitResponse recordVisit(Long ticketId, VisitRequest req) {
        HelpdeskVisit visit = HelpdeskVisit.builder()
                .ticketId(ticketId)
                .technicianId(req.getTechnicianId())
                .scheduledAt(req.getScheduledAt())
                .arrivedAt(req.getArrivedAt())
                .completedAt(req.getCompletedAt())
                .notes(req.getNotes())
                .build();
        return mapVisit(visitRepository.save(visit));
    }

    private VendorResponse mapVendor(HelpdeskVendor v) {
        return VendorResponse.builder()
                .id(v.getId())
                .communityId(v.getCommunityId())
                .name(v.getName())
                .contactPerson(v.getContactPerson())
                .phone(v.getPhone())
                .email(v.getEmail())
                .specializations(v.getSpecializations())
                .serviceAreas(v.getServiceAreas())
                .contractStartDate(v.getContractStartDate())
                .contractEndDate(v.getContractEndDate())
                .active(v.getActive())
                .totalTicketsCompleted(v.getTotalTicketsCompleted())
                .averageResponseMinutes(v.getAverageResponseMinutes())
                .averageResolutionMinutes(v.getAverageResolutionMinutes())
                .slaCompliancePercent(v.getSlaCompliancePercent())
                .averageRating(v.getAverageRating())
                .reopenedTickets(v.getReopenedTickets())
                .build();
    }

    private TechnicianResponse mapTechnician(HelpdeskTechnician t) {
        String vendorName = vendorRepository.findById(t.getVendorId())
                .map(HelpdeskVendor::getName).orElse(null);
        return TechnicianResponse.builder()
                .id(t.getId())
                .vendorId(t.getVendorId())
                .vendorName(vendorName)
                .communityId(t.getCommunityId())
                .name(t.getName())
                .phone(t.getPhone())
                .specialization(t.getSpecialization())
                .active(t.getActive())
                .currentWorkload(t.getCurrentWorkload())
                .build();
    }

    private VisitResponse mapVisit(HelpdeskVisit v) {
        String techName = technicianRepository.findById(v.getTechnicianId())
                .map(HelpdeskTechnician::getName).orElse(null);
        return VisitResponse.builder()
                .id(v.getId())
                .ticketId(v.getTicketId())
                .technicianId(v.getTechnicianId())
                .technicianName(techName)
                .scheduledAt(v.getScheduledAt())
                .arrivedAt(v.getArrivedAt())
                .completedAt(v.getCompletedAt())
                .notes(v.getNotes())
                .createdAt(v.getCreatedAt())
                .build();
    }
}
