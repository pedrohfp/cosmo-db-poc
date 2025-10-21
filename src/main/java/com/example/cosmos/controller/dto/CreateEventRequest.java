package com.example.cosmos.controller.dto;

import com.example.cosmos.model.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para criação de evento.
 */
public class CreateEventRequest {

    @NotBlank(message = "transactionId é obrigatório")
    private String transactionId;

    @NotNull(message = "eventType é obrigatório")
    private EventType eventType;

    public CreateEventRequest() {
    }

    public CreateEventRequest(String transactionId, EventType eventType) {
        this.transactionId = transactionId;
        this.eventType = eventType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "CreateEventRequest{" +
               "transactionId='" + transactionId + '\'' +
               ", eventType=" + eventType +
               '}';
    }
}

