package com.example.todospring.persistance;

import com.example.todospring.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
                                    // JpaRepository<T, ID> T: 테이블에 매핑될 엔티티
                                    //                      ID: 엔티티의 기본 키 타입
    // ?1은 메서드의 매개변수의 순서 위치를 말한다.
    @Query(value = "select * from Todo t where t.userId = ?1", nativeQuery = true)
    List<TodoEntity> findByUserId(String userId);




}

