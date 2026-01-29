package com.coursera.clinic.dto;

public class ErrorResponse {
    private boolean success;
    private String message;

    public ErrorResponse(String message) {
        this.success = false;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
