package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.enums.EscalationLevel;
import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.enums.TicketPriority;
import com.manacommunity.helpdesk.enums.TicketStatus;
import com.manacommunity.helpdesk.model.HelpdeskTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HelpdeskTicketRepository extends JpaRepository<HelpdeskTicket, Long> {

    Optional<HelpdeskTicket> findByTicketNumber(String ticketNumber);

    Page<HelpdeskTicket> findByCommunityIdAndReportedByUserId(Long communityId, Long reportedByUserId, Pageable pageable);

    Page<HelpdeskTicket> findByCommunityId(Long communityId, Pageable pageable);

    Page<HelpdeskTicket> findByCommunityIdAndStatusIn(Long communityId, List<TicketStatus> statuses, Pageable pageable);

    Page<HelpdeskTicket> findByCommunityIdAndCategory(Long communityId, HelpdeskCategory category, Pageable pageable);

    Page<HelpdeskTicket> findByCommunityIdAndAssignedVendorId(Long communityId, Long vendorId, Pageable pageable);

    Page<HelpdeskTicket> findByCommunityIdAndAssignedTechnicianId(Long communityId, Long technicianId, Pageable pageable);

    List<HelpdeskTicket> findByCommunityIdAndStatusNotInAndSlaDeadlineIsNotNull(
            Long communityId, List<TicketStatus> terminalStatuses);

    List<HelpdeskTicket> findByStatusNotInAndSlaDeadlineIsNotNull(List<TicketStatus> terminalStatuses);

    long countByCommunityIdAndStatusIn(Long communityId, List<TicketStatus> statuses);

    long countByCommunityIdAndCategory(Long communityId, HelpdeskCategory category);

    long countByCommunityIdAndPriority(Long communityId, TicketPriority priority);

    long countByCommunityIdAndEscalationLevel(Long communityId, EscalationLevel escalationLevel);

    @Query("SELECT t.category, count(t) FROM HelpdeskTicket t WHERE t.communityId = :communityId GROUP BY t.category")
    List<Object[]> countTicketsGroupedByCategory(@Param("communityId") Long communityId);

    @Query("SELECT t.tower, count(t) FROM HelpdeskTicket t WHERE t.communityId = :communityId AND t.tower IS NOT NULL GROUP BY t.tower")
    List<Object[]> countTicketsGroupedByTower(@Param("communityId") Long communityId);

    @Query("SELECT t.priority, count(t) FROM HelpdeskTicket t WHERE t.communityId = :communityId GROUP BY t.priority")
    List<Object[]> countTicketsGroupedByPriority(@Param("communityId") Long communityId);

    @Query("SELECT COUNT(t) FROM HelpdeskTicket t WHERE t.communityId = :communityId AND t.tower = :tower AND t.category = :category AND t.createdAt >= :since")
    long countRecentByTowerAndCategory(@Param("communityId") Long communityId,
                                       @Param("tower") String tower,
                                       @Param("category") HelpdeskCategory category,
                                       @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(t) FROM HelpdeskTicket t WHERE t.communityId = :communityId AND t.asset.id = :assetId AND t.createdAt >= :since")
    long countRecentByAsset(@Param("communityId") Long communityId,
                            @Param("assetId") Long assetId,
                            @Param("since") LocalDateTime since);
}
