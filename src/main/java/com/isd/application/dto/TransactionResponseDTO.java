package com.isd.application.dto;

import com.isd.application.commons.TransactionStatus;

import java.util.Date;

public class TransactionResponseDTO {

    private TransactionStatus status;
    private String message;
    private Date time;

    public TransactionResponseDTO() {
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
