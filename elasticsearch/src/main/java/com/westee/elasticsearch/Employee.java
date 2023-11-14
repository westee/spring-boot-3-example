package com.westee.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "employee")
public class Employee {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String department;
    // Getters and setters
}
