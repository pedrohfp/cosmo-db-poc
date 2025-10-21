package com.example.cosmos.controller;

import com.example.cosmos.controller.dto.CreateEventRequest;
import com.example.cosmos.controller.dto.EventResponse;
import com.example.cosmos.service.EventService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciar eventos.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
        log.info("EventController initialized");
    }

    /**
     * POST /api/events - Cria um novo evento
     * 
     * Body exemplo:
     * {
     *   "transactionId": "tx-12345",
     *   "eventType": "STARTED"
     * }
     */
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        log.info("POST /api/events - Creating event: {}", request);
        
        var event = eventService.createEvent(request.getTransactionId(), request.getEventType());
        var response = EventResponse.from(event);
        
        log.info("Event created successfully: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/events/{id}?transactionId={transactionId} - Busca um evento específico
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(
        @PathVariable String id,
        @RequestParam String transactionId
    ) {
        log.info("GET /api/events/{} - Getting event with transactionId={}", id, transactionId);
        
        var event = eventService.getEvent(id, transactionId);
        var response = EventResponse.from(event);
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/events?transactionId={transactionId} - Busca todos os eventos de uma transação
     */
    @GetMapping
    public ResponseEntity<List<EventResponse>> getEvents(@RequestParam(required = false) String transactionId) {
        log.info("GET /api/events - Getting events with transactionId={}", transactionId);
        
        List<EventResponse> responses;
        
        if (transactionId != null && !transactionId.isBlank()) {
            // Busca por transactionId específico
            var events = eventService.getEventsByTransactionId(transactionId);
            responses = events.stream()
                .map(EventResponse::from)
                .collect(Collectors.toList());
        } else {
            // Busca todos
            var events = eventService.getAllEvents();
            responses = events.stream()
                .map(EventResponse::from)
                .collect(Collectors.toList());
        }
        
        log.info("Found {} events", responses.size());
        return ResponseEntity.ok(responses);
    }

    /**
     * Exception handler para tratamento de erros.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Error processing request", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Classe interna para resposta de erro.
     */
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

