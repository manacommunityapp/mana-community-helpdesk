package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.ClassificationResult;

public interface ClassificationService {
    ClassificationResult classify(String rawDescription, Long communityId);
}
