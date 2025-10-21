package com.example.cosmos.repository;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.example.cosmos.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de eventos no Cosmos DB.
 */
@Repository
public class EventRepository {

    private static final Logger log = LoggerFactory.getLogger(EventRepository.class);
    private final CosmosContainer eventsContainer;

    public EventRepository(@Qualifier("events") CosmosContainer eventsContainer) {
        this.eventsContainer = eventsContainer;
        log.info("EventRepository initialized");
    }

    /**
     * Salva um evento no Cosmos DB.
     */
    public Event save(Event event) {
        log.info("Saving event: {}", event);
        try {
            eventsContainer.createItem(
                event,
                new PartitionKey(event.getTransactionId()),
                new CosmosItemRequestOptions()
            );
            log.info("Event saved successfully: id={}, transactionId={}", event.getId(), event.getTransactionId());
            return event;
        } catch (Exception e) {
            log.error("Error saving event: {}", event, e);
            throw new RuntimeException("Failed to save event", e);
        }
    }

    /**
     * Busca um evento por ID e transactionId (partition key).
     */
    public Optional<Event> findById(String id, String transactionId) {
        log.info("Finding event by id={}, transactionId={}", id, transactionId);
        try {
            var response = eventsContainer.readItem(
                id,
                new PartitionKey(transactionId),
                Event.class
            );
            Event event = response.getItem();
            log.info("Event found: {}", event);
            return Optional.ofNullable(event);
        } catch (Exception e) {
            log.warn("Event not found: id={}, transactionId={}", id, transactionId);
            return Optional.empty();
        }
    }

    /**
     * Busca todos os eventos de uma transação.
     */
    public List<Event> findByTransactionId(String transactionId) {
        log.info("Finding events by transactionId={}", transactionId);
        
        String query = "SELECT * FROM c WHERE c.transactionId = @transactionId";
        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        
        List<Event> events = new ArrayList<>();
        
        try {
            eventsContainer.queryItems(query, options, Event.class)
                .iterableByPage()
                .forEach(page -> events.addAll(page.getResults()));
            
            log.info("Found {} events for transactionId={}", events.size(), transactionId);
            return events;
        } catch (Exception e) {
            log.error("Error finding events by transactionId={}", transactionId, e);
            throw new RuntimeException("Failed to find events", e);
        }
    }

    /**
     * Busca todos os eventos.
     */
    public List<Event> findAll() {
        log.info("Finding all events");
        
        String query = "SELECT * FROM c";
        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        
        List<Event> events = new ArrayList<>();
        
        try {
            eventsContainer.queryItems(query, options, Event.class)
                .iterableByPage()
                .forEach(page -> events.addAll(page.getResults()));
            
            log.info("Found {} total events", events.size());
            return events;
        } catch (Exception e) {
            log.error("Error finding all events", e);
            throw new RuntimeException("Failed to find events", e);
        }
    }
}

