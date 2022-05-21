package com.example.todospring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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
    //@Column(name="id")
    private String id; // object id
    //@Column(name = "userId")
    private String userId;
    //@Column(name = "title")
    private String title; // todo title
    //@Column(name = "done")
    private boolean done;
}
