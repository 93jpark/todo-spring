package com.example.todospring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder // allow using build() method
@NoArgsConstructor // constructor with zero-args
@AllArgsConstructor // constructor with all fields
@Data // getter&setter
@Entity
@Table(name = "Todo")
public class TodoEntity {
    @Id
    @GeneratedValue(generator="system-uuid") // ID 자동 생성, generator: ID 생성방식
    @GenericGenerator(name="system-uuid", strategy="uuid")
    private String id; // object id
    private String userId;
    private String title; // todo title
    private boolean done;
}
