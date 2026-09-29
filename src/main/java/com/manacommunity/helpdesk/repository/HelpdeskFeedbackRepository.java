package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.model.HelpdeskFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HelpdeskFeedbackRepository extends JpaRepository<HelpdeskFeedback, Long> {
    Optional<HelpdeskFeedback> findByTicketId(Long ticketId);
    List<HelpdeskFeedback> findByCommunityId(Long communityId);
}
