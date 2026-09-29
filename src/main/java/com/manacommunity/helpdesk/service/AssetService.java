package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.AssetRequest;
import com.manacommunity.helpdesk.dto.AssetResponse;
import com.manacommunity.helpdesk.enums.AssetStatus;
import com.manacommunity.helpdesk.model.HelpdeskAsset;
import com.manacommunity.helpdesk.model.HelpdeskAssetMaintenanceLog;
import com.manacommunity.helpdesk.model.HelpdeskVendor;
import com.manacommunity.helpdesk.repository.HelpdeskAssetMaintenanceLogRepository;
import com.manacommunity.helpdesk.repository.HelpdeskAssetRepository;
import com.manacommunity.helpdesk.repository.HelpdeskVendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final HelpdeskAssetRepository assetRepository;
    private final HelpdeskAssetMaintenanceLogRepository maintenanceLogRepository;
    private final HelpdeskVendorRepository vendorRepository;

    public List<AssetResponse> getAssets(Long communityId) {
        return assetRepository.findByCommunityId(communityId)
                .stream().map(this::mapAsset).collect(Collectors.toList());
    }

    public AssetResponse getAssetById(Long id) {
        HelpdeskAsset a = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + id));
        return mapAsset(a);
    }

    @Transactional
    public AssetResponse createAsset(AssetRequest req, Long communityId) {
        HelpdeskAsset asset = HelpdeskAsset.builder()
                .communityId(communityId)
                .name(req.getName())
                .assetCode(req.getAssetCode())
                .category(req.getCategory())
                .towerLocation(req.getTowerLocation())
                .floorLocation(req.getFloorLocation())
                .areaLocation(req.getAreaLocation())
                .description(req.getDescription())
                .installationDate(req.getInstallationDate())
                .warrantyExpiry(req.getWarrantyExpiry())
                .status(req.getStatus() != null ? req.getStatus() : AssetStatus.ACTIVE)
                .lastMaintenanceDate(req.getLastMaintenanceDate())
                .nextMaintenanceDue(req.getNextMaintenanceDue())
                .vendorId(req.getVendorId())
                .build();
        return mapAsset(assetRepository.save(asset));
    }

    public List<HelpdeskAssetMaintenanceLog> getMaintenanceHistory(Long assetId) {
        return maintenanceLogRepository.findByAssetIdOrderByPerformedAtDesc(assetId);
    }

    private AssetResponse mapAsset(HelpdeskAsset a) {
        String vendorName = null;
        if (a.getVendorId() != null) {
            vendorName = vendorRepository.findById(a.getVendorId())
                    .map(HelpdeskVendor::getName).orElse(null);
        }
        return AssetResponse.builder()
                .id(a.getId())
                .communityId(a.getCommunityId())
                .name(a.getName())
                .assetCode(a.getAssetCode())
                .category(a.getCategory())
                .towerLocation(a.getTowerLocation())
                .floorLocation(a.getFloorLocation())
                .areaLocation(a.getAreaLocation())
                .description(a.getDescription())
                .installationDate(a.getInstallationDate())
                .warrantyExpiry(a.getWarrantyExpiry())
                .status(a.getStatus())
                .lastMaintenanceDate(a.getLastMaintenanceDate())
                .nextMaintenanceDue(a.getNextMaintenanceDue())
                .vendorId(a.getVendorId())
                .vendorName(vendorName)
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
