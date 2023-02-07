package com.reactiverest.studentapp.models;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@ToString
public class Student {

    // Document model for Student object
    @Id
    private String id;
    private String name;
    private String subject;
    private String email;
    private String address;
}
