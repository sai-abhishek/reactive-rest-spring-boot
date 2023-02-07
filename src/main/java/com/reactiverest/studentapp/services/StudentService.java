package com.reactiverest.studentapp.services;

import com.google.gson.Gson;
import com.reactiverest.studentapp.models.Student;
import com.reactiverest.studentapp.models.StudentNotFoundException;
import com.reactiverest.studentapp.repositories.StudentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    // CRUD operations for student
    
    public Flux<Student> getStudents() {
        return this.repository.findAll()
                .doOnNext(student -> logger.info("/GET all students, {}", new Gson().toJson(student)));
    }

    public Mono<Student> getStudent(final String id) {
        return this.repository.findById(id)
                .switchIfEmpty(Mono.error(new StudentNotFoundException(id)))
                .doOnNext(student -> logger.info("/GET student by ID , {}", new Gson().toJson(student)));
    }

    public Mono<Student> removeStudent(final String id) {
        return this.repository.findById(id)
                .switchIfEmpty(Mono.error(new StudentNotFoundException(id)))
                .flatMap(student -> this.repository.deleteById(id).thenReturn(student))
                .doOnNext(student -> logger.info("/DELETE student by ID, {}", new Gson().toJson(student)));
    }

    public Mono<Student> updateStudent(final Student student) {
        return this.repository.findById(student.getId())
                .switchIfEmpty(Mono.error(new StudentNotFoundException(student.getId())))
                .map(studentData -> student)
                .flatMap(this.repository::save)
                .doOnNext(studentData -> logger.info("/DELETE student by ID, {}", new Gson().toJson(studentData)));
    }

    public Mono<Student> addStudent(final Student student) {
        return this.repository.save(student)
                .doOnNext(studentData -> logger.info("/POST student by ID, {}", new Gson().toJson(studentData)));
    }

}
