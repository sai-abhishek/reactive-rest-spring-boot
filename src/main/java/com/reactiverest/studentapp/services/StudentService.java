package com.reactiverest.studentapp.services;

import com.reactiverest.studentapp.models.Student;
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
        return this.repository.findAll();
    }

    public Mono<Student> getStudent(final String id) {
        return this.repository.findById(id);
    }

    public Mono<Student> removeStudent(final String id) {
        return this.repository.findById(id)
                .flatMap(student -> this.repository.deleteById(id).thenReturn(student));
    }

    public Mono<Student> updateStudent(final Student student) {
        return this.repository.findById(student.getId())
                .map(studentData -> student)
                .flatMap(this.repository::save);
    }

    public Mono<Student> addStudent(final Student student) {
        return this.repository.save(student);
    }

}
