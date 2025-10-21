package com.example.cosmos.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

/**
 * Modelo de evento armazenado no Cosmos DB.
 */
public class Event {

    @JsonProperty("id")
    private String id;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("eventType")
    private EventType eventType;

    @JsonProperty("timestamp")
    private Instant timestamp;

    public Event() {
    }

    public Event(String id, String transactionId, EventType eventType, Instant timestamp) {
        this.id = id;
        this.transactionId = transactionId;
        this.eventType = eventType;
        this.timestamp = timestamp;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
               Objects.equals(transactionId, event.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionId);
    }

    @Override
    public String toString() {
        return "Event{" +
               "id='" + id + '\'' +
               ", transactionId='" + transactionId + '\'' +
               ", eventType=" + eventType +
               ", timestamp=" + timestamp +
               '}';
    }
}

