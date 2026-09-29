package com.manacommunity.helpdesk.controller;

import com.manacommunity.common.user.security.UserPrincipal;
import com.manacommunity.helpdesk.dto.WorkOrderRequest;
import com.manacommunity.helpdesk.dto.WorkOrderResponse;
import com.manacommunity.helpdesk.service.WorkOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helpdesk/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @GetMapping
    public ResponseEntity<List<WorkOrderResponse>> getWorkOrders(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(workOrderService.getWorkOrders(communityId));
    }

    @PostMapping
    public ResponseEntity<WorkOrderResponse> createWorkOrder(
            @Valid @RequestBody WorkOrderRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(workOrderService.createWorkOrder(req, communityId));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<WorkOrderResponse> completeWorkOrder(
            @PathVariable Long id,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(workOrderService.completeWorkOrder(id, notes));
    }
}
