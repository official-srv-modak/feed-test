package com.souravmodak.feedtest.models.nonentities.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class BaseResponse<T> {

    private T data;
    protected final String txnId;
    protected String txnDate;
    protected String txnTime;
    protected HttpStatus status;
    protected String message = "";

    public BaseResponse() {
        this.txnId = UUID.randomUUID().toString();
        this.txnDate = String.valueOf(java.time.LocalDate.now());
        this.txnTime = String.valueOf(java.time.LocalTime.now());
    }

    public BaseResponse(HttpStatus status, String message) {
        this.txnId = UUID.randomUUID().toString();;
        this.txnDate = String.valueOf(java.time.LocalDate.now());
        this.txnTime = String.valueOf(java.time.LocalTime.now());
        this.status = status;
        this.message = message;
    }

    public BaseResponse(T data, HttpStatus status, String message) {
        this.txnId = UUID.randomUUID().toString();;
        this.txnDate = String.valueOf(java.time.LocalDate.now());
        this.txnTime = String.valueOf(java.time.LocalTime.now());
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getTxnId() {
        return txnId;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // Fallback in case of an exception
        }
    }
}
