package com.um.springbootprojstructure.dto;

import java.time.Instant;

public class ApiErrorResponse {

    private Instant timestamp = Instant.now();
    private int httpStatus;
    private String code;      // stable machine-readable code
    private String message;   // human-readable message
    private String path;      // request path

    public ApiErrorResponse() {}

    public ApiErrorResponse(int httpStatus, String code, String message, String path) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() { return timestamp; }
    public int getHttpStatus() { return httpStatus; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getPath() { return path; }

    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public void setHttpStatus(int httpStatus) { this.httpStatus = httpStatus; }
    public void setCode(String code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setPath(String path) { this.path = path; }
}