package com.reactiverest.studentapp.handlers;

import com.reactiverest.studentapp.models.Student;
import com.reactiverest.studentapp.services.RateLimitService;
import com.reactiverest.studentapp.services.StudentService;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class StudentHandler {

    @Autowired
    private StudentService studentService;

    @Autowired
    private RateLimitService rateLimitService;

    // CRUD requests and responses filtered through rate limiter

    public Mono<ServerResponse> getStudents(ServerRequest request) {
        return defaultReadResponse(studentService.getStudents()
                .transformDeferred(RateLimiterOperator.of(rateLimitService.getRateLimiter())));
    }

    public Mono<ServerResponse> getStudent(ServerRequest request) {
        return defaultReadResponse(studentService.getStudent(parseId(request))
                .transformDeferred(RateLimiterOperator.of(rateLimitService.getRateLimiter())));
    }

    public Mono<ServerResponse> removeStudent(ServerRequest request) {
        return defaultReadResponse(this.studentService.removeStudent(parseId(request))
                .transformDeferred(RateLimiterOperator.of(rateLimitService.getRateLimiter())));
    }

    public Mono<ServerResponse> updateStudent(ServerRequest request) {
        Flux<Student> response = request.bodyToFlux(Student.class)
                .flatMap(student -> this.studentService.updateStudent(student));
        return defaultReadResponse(response
                .transformDeferred(RateLimiterOperator.of(rateLimitService.getRateLimiter())));
    }

    public Mono<ServerResponse> addStudent(ServerRequest request) {
        Flux<Student> response = request.bodyToFlux(Student.class)
                .flatMap(student -> this.studentService.addStudent(student));
        return defaultWriteResponse(response
                .transformDeferred(RateLimiterOperator.of(rateLimitService.getRateLimiter())));
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<Student> student) {
        return Mono.from(student)
                .flatMap(s -> ServerResponse
                        .created(URI.create("/students/" + s.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                );
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<Student> students) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(students, Student.class);
    }

    private static String parseId(ServerRequest request) {
        return request.pathVariable("id");
    }

}

