package com.example.todospring.persistance;

import com.example.todospring.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
                                    // JpaRepository<T, ID> T: 테이블에 매핑될 엔티티
                                    //                      ID: 엔티티의 기본 키 타입


}

