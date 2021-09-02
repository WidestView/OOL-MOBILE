package com.example.ool_mobile.service.api;

public class HttpStatusException extends RuntimeException {

    private int status;

    public HttpStatusException(int status) {
        super("Http response resulted with status code " + status);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
