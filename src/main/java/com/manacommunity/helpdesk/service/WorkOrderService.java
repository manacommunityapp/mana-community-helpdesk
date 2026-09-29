package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.WorkOrderRequest;
import com.manacommunity.helpdesk.dto.WorkOrderResponse;
import com.manacommunity.helpdesk.enums.WorkOrderType;
import com.manacommunity.helpdesk.model.HelpdeskAsset;
import com.manacommunity.helpdesk.model.HelpdeskTechnician;
import com.manacommunity.helpdesk.model.HelpdeskVendor;
import com.manacommunity.helpdesk.model.HelpdeskWorkOrder;
import com.manacommunity.helpdesk.repository.HelpdeskAssetRepository;
import com.manacommunity.helpdesk.repository.HelpdeskTechnicianRepository;
import com.manacommunity.helpdesk.repository.HelpdeskVendorRepository;
import com.manacommunity.helpdesk.repository.HelpdeskWorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final HelpdeskWorkOrderRepository workOrderRepository;
    private final HelpdeskAssetRepository assetRepository;
    private final HelpdeskVendorRepository vendorRepository;
    private final HelpdeskTechnicianRepository technicianRepository;

    public List<WorkOrderResponse> getWorkOrders(Long communityId) {
        return workOrderRepository.findByCommunityId(communityId)
                .stream().map(this::mapWorkOrder).collect(Collectors.toList());
    }

    @Transactional
    public WorkOrderResponse createWorkOrder(WorkOrderRequest req, Long communityId) {
        HelpdeskWorkOrder wo = HelpdeskWorkOrder.builder()
                .communityId(communityId)
                .assetId(req.getAssetId())
                .title(req.getTitle())
                .description(req.getDescription())
                .workOrderType(req.getWorkOrderType() != null ? req.getWorkOrderType() : WorkOrderType.PREVENTIVE)
                .scheduledDate(req.getScheduledDate())
                .status("SCHEDULED")
                .assignedVendorId(req.getAssignedVendorId())
                .assignedTechnicianId(req.getAssignedTechnicianId())
                .notes(req.getNotes())
                .build();
        return mapWorkOrder(workOrderRepository.save(wo));
    }

    @Transactional
    public WorkOrderResponse completeWorkOrder(Long id, String notes) {
        HelpdeskWorkOrder wo = workOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Work order not found: " + id));
        wo.setStatus("COMPLETED");
        wo.setCompletedAt(LocalDateTime.now());
        if (notes != null) {
            wo.setNotes(notes);
        }
        return mapWorkOrder(workOrderRepository.save(wo));
    }

    private WorkOrderResponse mapWorkOrder(HelpdeskWorkOrder wo) {
        String assetName = null;
        String assetCode = null;
        if (wo.getAssetId() != null) {
            HelpdeskAsset a = assetRepository.findById(wo.getAssetId()).orElse(null);
            if (a != null) {
                assetName = a.getName();
                assetCode = a.getAssetCode();
            }
        }
        String vendorName = wo.getAssignedVendorId() != null
                ? vendorRepository.findById(wo.getAssignedVendorId()).map(HelpdeskVendor::getName).orElse(null)
                : null;
        String techName = wo.getAssignedTechnicianId() != null
                ? technicianRepository.findById(wo.getAssignedTechnicianId()).map(HelpdeskTechnician::getName).orElse(null)
                : null;

        return WorkOrderResponse.builder()
                .id(wo.getId())
                .communityId(wo.getCommunityId())
                .assetId(wo.getAssetId())
                .assetName(assetName)
                .assetCode(assetCode)
                .title(wo.getTitle())
                .description(wo.getDescription())
                .workOrderType(wo.getWorkOrderType())
                .scheduledDate(wo.getScheduledDate())
                .status(wo.getStatus())
                .assignedVendorId(wo.getAssignedVendorId())
                .assignedVendorName(vendorName)
                .assignedTechnicianId(wo.getAssignedTechnicianId())
                .assignedTechnicianName(techName)
                .completedAt(wo.getCompletedAt())
                .notes(wo.getNotes())
                .createdAt(wo.getCreatedAt())
                .updatedAt(wo.getUpdatedAt())
                .build();
    }
}
