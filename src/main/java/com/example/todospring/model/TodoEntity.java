package com.example.todospring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder // allow using build() method
@NoArgsConstructor // constructor with zero-args
@AllArgsConstructor // constructor with all fields
@Data // getter&setter
public class TodoEntity {
    private String id; // object id
    private String userId;
    private String title; // todo title
    private boolean done;
}
