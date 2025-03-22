package com.credable.lms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScoringRestClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${credable.scoring.base-url}")
    private String baseUrl;

    @Value("${credable.scoring.client-token}")
    private String clientToken;

    public String initiateQueryScore(String customerNumber) {
        String url = baseUrl + "/scoring/initiateQueryScore/" + customerNumber;
        HttpHeaders headers = new HttpHeaders();
        headers.set("client-token", clientToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }

    public String queryScore(String token) {
        String url = baseUrl + "/scoring/queryScore/" + token;
        HttpHeaders headers = new HttpHeaders();
        headers.set("client-token", clientToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }
}
