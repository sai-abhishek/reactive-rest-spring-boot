package com.reactiverest.studentapp.models;

public class StudentNotFoundException extends RuntimeException {
    // Custom exception for Student record not found
    public StudentNotFoundException(String id) {
        super("Student:" + id + " is not found.");
    }
}
