package com.manacommunity.helpdesk.controller;

import com.manacommunity.common.user.security.UserPrincipal;
import com.manacommunity.helpdesk.dto.AssetRequest;
import com.manacommunity.helpdesk.dto.AssetResponse;
import com.manacommunity.helpdesk.model.HelpdeskAssetMaintenanceLog;
import com.manacommunity.helpdesk.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helpdesk/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAssets(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(assetService.getAssets(communityId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> getAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @PostMapping
    public ResponseEntity<AssetResponse> createAsset(
            @Valid @RequestBody AssetRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(assetService.createAsset(req, communityId));
    }

    @GetMapping("/{id}/maintenance-history")
    public ResponseEntity<List<HelpdeskAssetMaintenanceLog>> getMaintenanceHistory(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getMaintenanceHistory(id));
    }
}
