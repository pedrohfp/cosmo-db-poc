package com.example.cosmos.controller.dto;

import com.example.cosmos.model.Event;
import com.example.cosmos.model.EventType;

import java.time.Instant;

/**
 * DTO para resposta de evento.
 */
public class EventResponse {

    private String id;
    private String transactionId;
    private EventType eventType;
    private Instant timestamp;

    public EventResponse() {
    }

    public EventResponse(String id, String transactionId, EventType eventType, Instant timestamp) {
        this.id = id;
        this.transactionId = transactionId;
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    public static EventResponse from(Event event) {
        return new EventResponse(
            event.getId(),
            event.getTransactionId(),
            event.getEventType(),
            event.getTimestamp()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "EventResponse{" +
               "id='" + id + '\'' +
               ", transactionId='" + transactionId + '\'' +
               ", eventType=" + eventType +
               ", timestamp=" + timestamp +
               '}';
    }
}

