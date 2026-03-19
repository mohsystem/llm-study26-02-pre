package com.um.springbootprojstructure.dto;

public class NotificationSendRequest {
    private String to;
    private String message;

    public NotificationSendRequest() {}

    public NotificationSendRequest(String to, String message) {
        this.to = to;
        this.message = message;
    }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}