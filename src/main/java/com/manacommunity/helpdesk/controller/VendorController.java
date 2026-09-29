package com.manacommunity.helpdesk.controller;

import com.manacommunity.common.user.security.UserPrincipal;
import com.manacommunity.helpdesk.dto.*;
import com.manacommunity.helpdesk.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helpdesk/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    public ResponseEntity<List<VendorResponse>> getVendors(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(vendorService.getVendors(communityId));
    }

    @PostMapping
    public ResponseEntity<VendorResponse> createVendor(
            @Valid @RequestBody VendorRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorService.createVendor(req, communityId));
    }

    @GetMapping("/{id}/scorecard")
    public ResponseEntity<VendorScorecardResponse> getScorecard(
            @PathVariable Long id) {
        return ResponseEntity.ok(vendorService.getScorecard(id));
    }

    @GetMapping("/{id}/technicians")
    public ResponseEntity<List<TechnicianResponse>> getTechnicians(
            @PathVariable Long id) {
        return ResponseEntity.ok(vendorService.getTechnicians(id));
    }

    @PostMapping("/{id}/technicians")
    public ResponseEntity<TechnicianResponse> createTechnician(
            @PathVariable Long id,
            @Valid @RequestBody TechnicianRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        req.setVendorId(id);
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorService.createTechnician(req, communityId));
    }
}
