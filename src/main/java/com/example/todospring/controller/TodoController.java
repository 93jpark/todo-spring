package com.example.todospring.controller;

import com.example.todospring.dto.ResponseDTO;
import com.example.todospring.dto.TodoDTO;
import com.example.todospring.model.TodoEntity;
import com.example.todospring.service.TodoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService(); // test서비스 사용
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String tempUserId = "temp-user"; // temp user id

            // TodoEntity로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            // id를 null로 초기화
            entity.setUserId(null);

            // 임시 사용자 아이디 설정.
            entity.setUserId(tempUserId);

            // 서비스를 이용해 Todo 엔티티 생성
            List<TodoEntity> entities = service.create(entity);

            // 자바 스트림을 통해 리턴된 엔티티 리스틀 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // ResponseDTO 리턴
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 혹 예외가 있으면 dto 대신 error메시지 삽입
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        String tempUserId = "temp-user";

        // 서비스 메소드의 retrive() 메소드를 사용해 Todo 리스트를 가져옴
        List<TodoEntity> entities = service.retrieve(tempUserId);

        for(TodoEntity t : entities) {
            System.out.println(t.getTitle());
        }

        // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // ResponseDTO 리턴
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
        String tempUserId = "temp-user";

        // dto to entity transfer
        TodoEntity entity = TodoDTO.toEntity(dto);

        // initialize id
        entity.setUserId(tempUserId);

        // update() from service
        List<TodoEntity> entities = service.update(entity);

        // entity list transfer to TodoDTO list
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
        String tempUserId = "temp-user";

        // dto to entity transfer
        TodoEntity entity = TodoDTO.toEntity(dto);

        // initialize id
        entity.setUserId(tempUserId);

        // update() from service
        List<TodoEntity> entities = service.remove(entity);

        // entity list transfer to TodoDTO list
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

}
