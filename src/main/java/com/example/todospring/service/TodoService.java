package com.example.todospring.service;

import com.example.todospring.model.TodoEntity;
import com.example.todospring.persistance.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {
    @Autowired
    private TodoRepository repository;

    public String testService() {
        // TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();

        // TodoEntity 저장
        repository.save(entity);

        // TodoEntity 검색
        TodoEntity savedEntity = repository.findById(entity.getId()).get();

        return savedEntity.getTitle();
    }

    public List<TodoEntity> create(final TodoEntity entity) {
        validate(entity);

        repository.save(entity);

        log.info("Entity Id : {} is saved", entity.getId());

        return repository.findByUserId(entity.getUserId());

    }

    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity) {
        // 유효성 검증
        validate(entity);

        // 해당 id를 통해 TodoEntity 로드
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save(todo);
        });

        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> remove(final TodoEntity entity) {
        // 유효성 검증
        validate(entity);

        try {
            // 해당 entity를 통해 삭제
            repository.delete(entity);
        } catch(Exception e) {
            log.error("error deleting entity", entity.getId(), e);
            throw new RuntimeException("error deleting entity" + entity.getId());
        }
        return repository.findAll();
    }

    private void validate(TodoEntity entity) {
        // Validations
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
