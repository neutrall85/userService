package ru.aston.homework.intensive.apigateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.homework.intensive.circuitbreaker.service.CircuitBreakerService;

import java.util.Map;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final CircuitBreakerService circuitBreakerService;

    public GatewayController(CircuitBreakerService circuitBreakerService) {
        this.circuitBreakerService = circuitBreakerService;
    }

    @GetMapping("/circuit-breakers")
    public ResponseEntity<Map<String, Map<String, Object>>> getAllCircuitBreakers() {
        return ResponseEntity.ok(circuitBreakerService.getAllCircuitBreakers());
    }

    @GetMapping("/circuit-breakers/{name}")
    public ResponseEntity<Map<String, Object>> getCircuitBreaker(@PathVariable String name) {
        Map<String, Object> state = circuitBreakerService.getCircuitBreakerState(name);
        if (state.containsKey("error")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(state);
    }

    @PostMapping("/circuit-breakers/{name}/reset")
    public ResponseEntity<Map<String, String>> resetCircuitBreaker(@PathVariable String name) {
        try {
            circuitBreakerService.resetCircuitBreaker(name);
            return ResponseEntity.ok(Map.of(
                    "circuitBreaker", name,
                    "action", "reset",
                    "status", "success"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "circuitBreaker", name,
                    "action", "reset",
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/health")
    public String health() {
        return "API Gateway Management API is healthy";
    }
}
