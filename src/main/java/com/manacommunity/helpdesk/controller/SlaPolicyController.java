package com.manacommunity.helpdesk.controller;

import com.manacommunity.common.user.security.UserPrincipal;
import com.manacommunity.helpdesk.dto.SlaPolicyRequest;
import com.manacommunity.helpdesk.dto.SlaPolicyResponse;
import com.manacommunity.helpdesk.service.SlaPolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helpdesk/sla-policies")
@RequiredArgsConstructor
public class SlaPolicyController {

    private final SlaPolicyService slaPolicyService;

    @GetMapping
    public ResponseEntity<List<SlaPolicyResponse>> getPolicies(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(slaPolicyService.getPolicies(communityId));
    }

    @PostMapping
    public ResponseEntity<SlaPolicyResponse> saveOrUpdatePolicy(
            @Valid @RequestBody SlaPolicyRequest req,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long communityId = principal.getAppUser().getCommunity().getId();
        return ResponseEntity.ok(slaPolicyService.saveOrUpdatePolicy(req, communityId));
    }
}
