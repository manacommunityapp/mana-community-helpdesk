package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.CommentRequest;
import com.manacommunity.helpdesk.dto.CommentResponse;
import com.manacommunity.helpdesk.model.HelpdeskComment;
import com.manacommunity.helpdesk.repository.HelpdeskCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final HelpdeskCommentRepository commentRepository;

    public List<CommentResponse> getComments(Long ticketId, boolean isStaffOrAdmin) {
        List<HelpdeskComment> list = isStaffOrAdmin
                ? commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId)
                : commentRepository.findByTicketIdAndInternalOnlyFalseOrderByCreatedAtAsc(ticketId);
        return list.stream().map(this::mapComment).collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse addComment(Long ticketId, Long communityId, CommentRequest req,
                                       Long authorUserId, String authorName, String authorRole) {
        HelpdeskComment comment = HelpdeskComment.builder()
                .ticketId(ticketId)
                .communityId(communityId)
                .authorUserId(authorUserId)
                .authorName(authorName)
                .authorRole(authorRole)
                .message(req.getMessage())
                .internalOnly(Boolean.TRUE.equals(req.getInternalOnly()))
                .build();
        return mapComment(commentRepository.save(comment));
    }

    public CommentResponse mapComment(HelpdeskComment c) {
        return CommentResponse.builder()
                .id(c.getId())
                .ticketId(c.getTicketId())
                .authorUserId(c.getAuthorUserId())
                .authorName(c.getAuthorName())
                .authorRole(c.getAuthorRole())
                .message(c.getMessage())
                .internalOnly(c.getInternalOnly())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
