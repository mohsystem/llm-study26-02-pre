package com.um.springbootprojstructure.service;

public interface NotificationGatewayClient {
    void sendSms(String toPhoneNumber, String message);
}