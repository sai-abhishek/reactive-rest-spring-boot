package com.reactiverest.studentapp.handlers;

import com.reactiverest.studentapp.models.StudentNotFoundException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class CustomExceptionHandler implements WebExceptionHandler {
    
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        if (ex instanceof StudentNotFoundException) {
            // Custom exception handler for Student Not Found
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return exchange.getResponse().setComplete();

        } else if (ex instanceof RequestNotPermitted) {
            // Custom exception handler for Rate limited request
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();

        }
        return Mono.error(ex);
    }

}