package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.config.NotificationProperties;
import com.um.springbootprojstructure.dto.NotificationSendRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationGatewayClientImpl implements NotificationGatewayClient {

    private final RestClient restClient;
    private final NotificationProperties props;

    public NotificationGatewayClientImpl(NotificationProperties props) {
        this.props = props;
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-API-KEY", props.getApiKey())
                .build();
    }

    @Override
    public void sendSms(String toPhoneNumber, String message) {
        // Example gateway endpoint: POST /api/notify/sms
        // Body: { "to": "...", "message": "..." }
        restClient.post()
                .uri("/api/notify/sms")
                .body(new NotificationSendRequest(toPhoneNumber, message))
                .retrieve()
                .toBodilessEntity();
    }
}