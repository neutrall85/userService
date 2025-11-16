package ru.aston.homework.intensive.circuitbreaker.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Service
public class CircuitBreakerService {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerService(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public <T> T executeWithCircuitBreaker(String circuitBreakerName, Supplier<T> supplier) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        return circuitBreaker.executeSupplier(supplier);
    }

    public void executeWithCircuitBreaker(String circuitBreakerName, Runnable runnable) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        circuitBreaker.executeRunnable(runnable);
    }

    public Map<String, Object> getCircuitBreakerState(String name) {
        try {
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(name);
            return Map.of(
                    "name", name,
                    "state", circuitBreaker.getState().name(),
                    "failureRate", circuitBreaker.getMetrics().getFailureRate(),
                    "bufferedCalls", circuitBreaker.getMetrics().getNumberOfBufferedCalls(),
                    "failedCalls", circuitBreaker.getMetrics().getNumberOfFailedCalls(),
                    "successfulCalls", circuitBreaker.getMetrics().getNumberOfSuccessfulCalls(),
                    "notPermittedCalls", circuitBreaker.getMetrics().getNumberOfNotPermittedCalls()
            );
        } catch (Exception e) {
            return Map.of(
                    "error", "CircuitBreaker not found: " + name,
                    "message", e.getMessage()
            );
        }
    }

    public Map<String, Map<String, Object>> getAllCircuitBreakers() {
        Map<String, Map<String, Object>> circuitBreakers = new ConcurrentHashMap<>();
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb ->
                circuitBreakers.put(cb.getName(), getCircuitBreakerState(cb.getName())));
        return circuitBreakers;
    }

    public void resetCircuitBreaker(String name) {
        try {
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(name);
            circuitBreaker.reset();
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset circuit breaker: " + name, e);
        }
    }
}
