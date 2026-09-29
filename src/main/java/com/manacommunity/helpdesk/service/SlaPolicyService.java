package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.SlaPolicyRequest;
import com.manacommunity.helpdesk.dto.SlaPolicyResponse;
import com.manacommunity.helpdesk.model.HelpdeskSlaPolicyConfig;
import com.manacommunity.helpdesk.repository.HelpdeskSlaPolicyConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlaPolicyService {

    private final HelpdeskSlaPolicyConfigRepository slaPolicyConfigRepository;

    public List<SlaPolicyResponse> getPolicies(Long communityId) {
        return slaPolicyConfigRepository.findByCommunityIdAndActiveTrue(communityId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public SlaPolicyResponse saveOrUpdatePolicy(SlaPolicyRequest req, Long communityId) {
        HelpdeskSlaPolicyConfig policy = slaPolicyConfigRepository.findByCommunityIdAndPriority(communityId, req.getPriority())
                .orElse(HelpdeskSlaPolicyConfig.builder().communityId(communityId).priority(req.getPriority()).build());

        policy.setResolutionHours(req.getResolutionHours());
        policy.setEscalation75PercentMinutes(req.getEscalation75PercentMinutes() != null
                ? req.getEscalation75PercentMinutes()
                : (int) (req.getResolutionHours() * 60 * 0.75));
        policy.setEscalation90PercentMinutes(req.getEscalation90PercentMinutes() != null
                ? req.getEscalation90PercentMinutes()
                : (int) (req.getResolutionHours() * 60 * 0.90));
        policy.setActive(req.getActive() != null ? req.getActive() : true);

        return mapToResponse(slaPolicyConfigRepository.save(policy));
    }

    private SlaPolicyResponse mapToResponse(HelpdeskSlaPolicyConfig p) {
        return SlaPolicyResponse.builder()
                .id(p.getId())
                .communityId(p.getCommunityId())
                .priority(p.getPriority())
                .resolutionHours(p.getResolutionHours())
                .escalation75PercentMinutes(p.getEscalation75PercentMinutes())
                .escalation90PercentMinutes(p.getEscalation90PercentMinutes())
                .active(p.getActive())
                .build();
    }
}
