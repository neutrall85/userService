package ru.aston.homework.intensive.circuitbreaker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.homework.intensive.circuitbreaker.service.CircuitBreakerService;

import java.util.Map;

@RestController
@RequestMapping("/api/circuit-breakers")
public class CircuitBreakerController {

    private final CircuitBreakerService circuitBreakerService;

    public CircuitBreakerController(CircuitBreakerService circuitBreakerService) {
        this.circuitBreakerService = circuitBreakerService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Map<String, Object>>> getAllCircuitBreakers() {
        return ResponseEntity.ok(circuitBreakerService.getAllCircuitBreakers());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getCircuitBreaker(@PathVariable String name) {
        Map<String, Object> state = circuitBreakerService.getCircuitBreakerState(name);
        if (state.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(state);
        }
        return ResponseEntity.ok(state);
    }

    @PostMapping("/{name}/reset")
    public ResponseEntity<Map<String, String>> resetCircuitBreaker(@PathVariable String name) {
        try {
            circuitBreakerService.resetCircuitBreaker(name);
            return ResponseEntity.ok(Map.of(
                    "circuitBreaker", name,
                    "action", "reset",
                    "status", "success"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "circuitBreaker", name,
                    "action", "reset",
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/health")
    public String health() {
        return "Circuit Breaker Module is healthy";
    }
}
