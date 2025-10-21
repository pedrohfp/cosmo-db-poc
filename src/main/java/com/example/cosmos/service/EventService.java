package com.example.cosmos.service;

import com.example.cosmos.model.Event;
import com.example.cosmos.model.EventType;
import com.example.cosmos.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Service para lógica de negócio de eventos.
 */
@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        log.info("EventService initialized");
    }

    /**
     * Cria um novo evento com transactionId gerado automaticamente.
     */
    public Event createEvent(EventType eventType) {
        String transactionId = UUID.randomUUID().toString();
        log.info("Creating event with auto-generated transactionId={}, eventType={}", transactionId, eventType);
        
        Event event = new Event();
        event.setId(UUID.randomUUID().toString());
        event.setTransactionId(transactionId);
        event.setEventType(eventType);
        event.setTimestamp(Instant.now());
        
        return eventRepository.save(event);
    }

    /**
     * Cria um novo evento com transactionId específico (método mantido para compatibilidade).
     */
    public Event createEvent(String transactionId, EventType eventType) {
        log.info("Creating event: transactionId={}, eventType={}", transactionId, eventType);
        
        Event event = new Event();
        event.setId(UUID.randomUUID().toString());
        event.setTransactionId(transactionId);
        event.setEventType(eventType);
        event.setTimestamp(Instant.now());
        
        return eventRepository.save(event);
    }

    /**
     * Busca um evento por ID e transactionId.
     */
    public Event getEvent(String id, String transactionId) {
        log.info("Getting event: id={}, transactionId={}", id, transactionId);
        return eventRepository.findById(id, transactionId)
            .orElseThrow(() -> new RuntimeException("Event not found: id=" + id + ", transactionId=" + transactionId));
    }

    /**
     * Busca todos os eventos de uma transação.
     */
    public List<Event> getEventsByTransactionId(String transactionId) {
        log.info("Getting events by transactionId={}", transactionId);
        return eventRepository.findByTransactionId(transactionId);
    }

    /**
     * Busca todos os eventos.
     */
    public List<Event> getAllEvents() {
        log.info("Getting all events");
        return eventRepository.findAll();
    }
}

