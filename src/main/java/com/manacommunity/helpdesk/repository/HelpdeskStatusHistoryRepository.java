package com.manacommunity.helpdesk.repository;

import com.manacommunity.helpdesk.model.HelpdeskStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpdeskStatusHistoryRepository extends JpaRepository<HelpdeskStatusHistory, Long> {
    List<HelpdeskStatusHistory> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
