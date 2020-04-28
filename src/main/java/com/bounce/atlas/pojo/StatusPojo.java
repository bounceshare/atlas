package com.bounce.atlas.pojo;

import java.util.Map;

public class StatusPojo {

    private int statusCode;
    private String message;
    private String errorReason;
    private Map<Object, Object> data;

    public StatusPojo(int statusCode, String message, String errorReason) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.errorReason = errorReason;
    }

    public StatusPojo(int statusCode, String message, String errorReason, Map<Object, Object> data) {
        this.statusCode = statusCode;
        this.message = message;
        this.errorReason = errorReason;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public static StatusPojo buildSuccess() {
        return new StatusPojo(200, "SUCCESS", "");
    }

    public static StatusPojo buildSuccess(Map<Object, Object> data) {
        return new StatusPojo(200, "SUCCESS", "", data);
    }

    public static StatusPojo buildFailure(int code, String reason) {
        return new StatusPojo(code, "FAIL", reason);
    }
}
