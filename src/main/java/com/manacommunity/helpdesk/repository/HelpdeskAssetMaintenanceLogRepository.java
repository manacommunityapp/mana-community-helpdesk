package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.model.HelpdeskAssetMaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpdeskAssetMaintenanceLogRepository extends JpaRepository<HelpdeskAssetMaintenanceLog, Long> {
    List<HelpdeskAssetMaintenanceLog> findByAssetIdOrderByPerformedAtDesc(Long assetId);
    List<HelpdeskAssetMaintenanceLog> findByCommunityIdOrderByPerformedAtDesc(Long communityId);
}
