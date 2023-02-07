package com.reactiverest.studentapp.repositories;

import com.reactiverest.studentapp.models.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends ReactiveMongoRepository<Student, String> {
    // Wiring our repository to use built-in methods from ReactiveMongoRepository
}
