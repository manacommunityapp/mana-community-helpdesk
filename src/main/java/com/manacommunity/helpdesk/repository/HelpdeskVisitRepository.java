package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.model.HelpdeskVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpdeskVisitRepository extends JpaRepository<HelpdeskVisit, Long> {
    List<HelpdeskVisit> findByTicketIdOrderByScheduledAtAsc(Long ticketId);
}
