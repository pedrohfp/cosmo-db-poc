package com.example.cosmos.controller.dto;

import com.example.cosmos.model.EventType;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para criação de evento.
 */
public class CreateEventRequest {

    @NotNull(message = "eventType é obrigatório")
    private EventType eventType;

    public CreateEventRequest() {
    }

    public CreateEventRequest(EventType eventType) {
        this.eventType = eventType;
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
               "eventType=" + eventType +
               '}';
    }
}

