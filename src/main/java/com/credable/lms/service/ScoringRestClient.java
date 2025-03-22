package com.credable.lms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScoringRestClient {
    @Value("${credable.scoring.base-url}")
    private String baseUrl;

    @Value("${credable.scoring.client-token}")
    private String clientToken;

    public String initiateQueryScore(String customerNumber) {
        return "mock-token-" + customerNumber; // Mock token
    }

    public String queryScore(String token) {
        return "{\"score\": 600, \"limitAmount\": 10000}"; // Mock APPROVED response
    }
}
