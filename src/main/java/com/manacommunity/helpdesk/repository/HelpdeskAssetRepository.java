package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.enums.AssetStatus;
import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.model.HelpdeskAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HelpdeskAssetRepository extends JpaRepository<HelpdeskAsset, Long> {
    List<HelpdeskAsset> findByCommunityId(Long communityId);
    List<HelpdeskAsset> findByCommunityIdAndCategory(Long communityId, HelpdeskCategory category);
    List<HelpdeskAsset> findByCommunityIdAndStatus(Long communityId, AssetStatus status);
    Optional<HelpdeskAsset> findByCommunityIdAndAssetCode(Long communityId, String assetCode);
    List<HelpdeskAsset> findByCommunityIdAndNextMaintenanceDueBefore(Long communityId, LocalDate date);
}
