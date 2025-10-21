package com.example.cosmos.controller.dto;

/**
 * DTO para resposta de evento criado com sucesso.
 */
public class EventCreatedResponse {

    private String message;
    private String transactionId;

    public EventCreatedResponse() {
    }

    public EventCreatedResponse(String message, String transactionId) {
        this.message = message;
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "EventCreatedResponse{" +
               "message='" + message + '\'' +
               ", transactionId='" + transactionId + '\'' +
               '}';
    }
}
