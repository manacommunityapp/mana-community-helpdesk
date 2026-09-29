package com.manacommunity.helpdesk.service;

import com.manacommunity.helpdesk.dto.ClassificationResult;
import com.manacommunity.helpdesk.enums.ClassificationSource;
import com.manacommunity.helpdesk.enums.HelpdeskCategory;
import com.manacommunity.helpdesk.enums.TicketPriority;
import com.manacommunity.helpdesk.model.HelpdeskSubcategory;
import com.manacommunity.helpdesk.repository.HelpdeskSubcategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleBasedClassificationService implements ClassificationService {

    private final HelpdeskSubcategoryRepository subcategoryRepository;

    // Emergency keywords that trigger immediate CRITICAL safety warnings
    private static final Set<String> EMERGENCY_KEYWORDS = Set.of(
            "fire", "smoke", "burning", "gas leak", "smell gas", "cylinder leak",
            "trapped", "stuck inside", "person stuck", "electric shock", "sparking",
            "short circuit", "collapse", "massive flood", "intruder", "theft", "violence"
    );

    @Override
    public ClassificationResult classify(String rawDescription, Long communityId) {
        if (rawDescription == null || rawDescription.trim().isEmpty()) {
            return fallbackResult();
        }

        String normalized = rawDescription.toLowerCase(Locale.ROOT);

        // 1. Safety / Emergency Check
        boolean isEmergency = EMERGENCY_KEYWORDS.stream().anyMatch(normalized::contains);

        // 2. Query configured active subcategories for this community
        List<HelpdeskSubcategory> subcategories = subcategoryRepository.findByCommunityIdAndActiveTrue(communityId);

        HelpdeskSubcategory bestMatch = null;
        int maxKeywordHits = 0;

        for (HelpdeskSubcategory sub : subcategories) {
            if (sub.getKeywords() != null && !sub.getKeywords().isBlank()) {
                String[] keywords = sub.getKeywords().split(",");
                int hits = 0;
                for (String kw : keywords) {
                    String cleanKw = kw.trim().toLowerCase(Locale.ROOT);
                    if (!cleanKw.isEmpty() && normalized.contains(cleanKw)) {
                        hits++;
                    }
                }
                if (hits > maxKeywordHits) {
                    maxKeywordHits = hits;
                    bestMatch = sub;
                }
            }
        }

        // 3. Fallback to category guessing if no subcategory matched
        HelpdeskCategory category = HelpdeskCategory.MAINTENANCE;
        TicketPriority priority = TicketPriority.MEDIUM;
        int slaHours = 24;
        int confidence = 50;
        String subcategoryName = "General";
        Long subcategoryId = null;
        String suggestedTeam = "Facility Management";

        if (bestMatch != null) {
            category = bestMatch.getCategory();
            subcategoryId = bestMatch.getId();
            subcategoryName = bestMatch.getName();
            priority = bestMatch.getDefaultPriority() != null ? bestMatch.getDefaultPriority() : TicketPriority.MEDIUM;
            slaHours = bestMatch.getDefaultSlaDurationHours() != null ? bestMatch.getDefaultSlaDurationHours() : 24;
            confidence = Math.min(95, 60 + (maxKeywordHits * 12));
            suggestedTeam = subcategoryName + " Team";
        } else {
            // Static rule matching
            if (normalized.contains("water") || normalized.contains("leak") || normalized.contains("pipe") || normalized.contains("tap")) {
                category = HelpdeskCategory.MAINTENANCE;
                subcategoryName = "Plumbing";
                priority = TicketPriority.HIGH;
                slaHours = 4;
                confidence = 85;
                suggestedTeam = "Plumbing Team";
            } else if (normalized.contains("lift") || normalized.contains("elevator")) {
                category = HelpdeskCategory.MAINTENANCE;
                subcategoryName = "Lift";
                priority = TicketPriority.HIGH;
                slaHours = 4;
                confidence = 90;
                suggestedTeam = "Lift Maintenance Team";
            } else if (normalized.contains("power") || normalized.contains("light") || normalized.contains("switch") || normalized.contains("electricity")) {
                category = HelpdeskCategory.MAINTENANCE;
                subcategoryName = "Electrical";
                priority = TicketPriority.HIGH;
                slaHours = 4;
                confidence = 85;
                suggestedTeam = "Electrical Team";
            } else if (normalized.contains("garbage") || normalized.contains("waste") || normalized.contains("dustbin") || normalized.contains("cleaning") || normalized.contains("sweep")) {
                category = HelpdeskCategory.COMMUNITY;
                subcategoryName = "Cleaning & Waste";
                priority = TicketPriority.MEDIUM;
                slaHours = 8;
                confidence = 80;
                suggestedTeam = "Housekeeping Team";
            } else if (normalized.contains("security") || normalized.contains("guard") || normalized.contains("gate") || normalized.contains("visitor")) {
                category = HelpdeskCategory.COMMUNITY;
                subcategoryName = "Security";
                priority = TicketPriority.HIGH;
                slaHours = 4;
                confidence = 85;
                suggestedTeam = "Security Team";
            } else if (normalized.contains("parking") || normalized.contains("vehicle") || normalized.contains("car") || normalized.contains("bike")) {
                category = HelpdeskCategory.COMMUNITY;
                subcategoryName = "Parking";
                priority = TicketPriority.MEDIUM;
                slaHours = 24;
                confidence = 75;
                suggestedTeam = "Estate Team";
            } else if (normalized.contains("bill") || normalized.contains("payment") || normalized.contains("receipt") || normalized.contains("dues")) {
                category = HelpdeskCategory.ADMINISTRATION;
                subcategoryName = "Billing & Payments";
                priority = TicketPriority.LOW;
                slaHours = 48;
                confidence = 80;
                suggestedTeam = "Accounts & Admin";
            }
        }

        // 4. Overriding safety rule: Emergencies MUST be CRITICAL
        if (isEmergency) {
            category = HelpdeskCategory.SAFETY;
            priority = TicketPriority.CRITICAL;
            slaHours = 0; // Immediate
            confidence = 99;
            suggestedTeam = "Emergency Response Team";
        }

        log.info("Classified ticket [emergency={}] -> category={}, subcategory={}, priority={}, confidence={}%",
                isEmergency, category, subcategoryName, priority, confidence);

        return ClassificationResult.builder()
                .category(category)
                .subcategoryId(subcategoryId)
                .subcategoryName(subcategoryName)
                .priority(priority)
                .suggestedSlaHours(slaHours)
                .confidence(confidence)
                .emergencyFlag(isEmergency)
                .source(ClassificationSource.RULE_DETERMINED)
                .suggestedTeam(suggestedTeam)
                .build();
    }

    private ClassificationResult fallbackResult() {
        return ClassificationResult.builder()
                .category(HelpdeskCategory.MAINTENANCE)
                .subcategoryName("General")
                .priority(TicketPriority.MEDIUM)
                .suggestedSlaHours(24)
                .confidence(50)
                .emergencyFlag(false)
                .source(ClassificationSource.RULE_DETERMINED)
                .suggestedTeam("Facility Management")
                .build();
    }
}
