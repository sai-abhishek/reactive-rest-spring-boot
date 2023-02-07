package com.reactiverest.studentapp.routers;

import com.reactiverest.studentapp.handlers.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class StudentRouter {
    // Defining all endpoints and associated handlers
    @Bean
    public RouterFunction<ServerResponse> routes(StudentHandler studentHandler) {
        return route(GET("/students"), studentHandler::getStudents)
                .andRoute(GET("/students/{id}"), studentHandler::getStudent)
                .andRoute(DELETE("/students/{id}"), studentHandler::removeStudent)
                .andRoute(POST("/students"), studentHandler::addStudent)
                .andRoute(PUT("/students"), studentHandler::updateStudent);
    }

}