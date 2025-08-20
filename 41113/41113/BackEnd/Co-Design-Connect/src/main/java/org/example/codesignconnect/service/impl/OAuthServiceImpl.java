package org.example.codesignconnect.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.mapper.TokenMapper;
import org.example.codesignconnect.model.Token;
import org.example.codesignconnect.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OAuthServiceImpl implements OAuthService {
    @Autowired
    private TokenMapper tokenMapper;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper mapper = new ObjectMapper();
    @Value("${miro.client-id}")
    private String miroClientId;
    @Value("${miro.client-secret}")
    private String miroClientSecret;

    @Override
    public Token refreshMiroToken() {
        try {
            String url = "https://api.miro.com/v1/oauth/token";
            String refreshToken = tokenMapper.getTokenByType("miro").getRefreshToken();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "refresh_token");
            params.add("client_id", miroClientId);
            params.add("client_secret", miroClientSecret);
            params.add("refresh_token", refreshToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            JsonNode root = mapper.readTree(response.getBody());
            String newAccessToken = root.path("access_token").asText();
            String newRefreshToken = root.path("refresh_token").asText();
            Token token = new Token(null, "miro", newRefreshToken, newAccessToken);
            tokenMapper.updateTokenByType(token);
            return tokenMapper.getTokenByType("miro");
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public Token refreshGoogleDocsToken() {
        return null;
    }
}
