package com.coursera.clinic.dto;

public class SuccessResponse<T> {
    private boolean success;
    private T data;

    public SuccessResponse(T data) {
        this.success = true;
        this.data = data;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
