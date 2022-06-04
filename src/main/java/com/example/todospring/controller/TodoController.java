package com.example.todospring.controller;

import com.example.todospring.dto.ResponseDTO;
import com.example.todospring.dto.TodoDTO;
import com.example.todospring.model.TodoEntity;
import com.example.todospring.service.TodoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins="http://localhost:3000", maxAge=3600)
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
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {

        try {
            // 1 TodoEntity로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            // 2 id를 null로 초기화한다. 생성 당시 id가 없어야 하기 때문이다.
            entity.setId(null);

            // 3 임시 사용자 아이디를 설정해 준다. // 기존 temporary-user 대신 @AuthenticationPrincipal에서 넘어온 userId로 설정
            entity.setUserId(userId);

            // 4 서비스를 이용해 Todo엔티티를 생성한다.
            List<TodoEntity> entities = service.create(entity);

            // 5 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 6 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // 7 ResponseDTO를 리턴한다
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 8 혹 예외가 있으면 dto 대신 error메시지 삽입
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
        //String tempUserId = "temp-user";

        // 1 서비스 메소드의 retrive() 메소드를 사용해 Todo리스트를 가져옴.
        List<TodoEntity> entities = service.retrieve(userId);

        // 2 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 3 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // ResponseDTO 리턴
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {

        // 1 dto를 entity로 변환해준다.
        TodoEntity entity = TodoDTO.toEntity(dto);

        // 2 id를 userId로 초기화한다.
        entity.setUserId(userId);

        // 3 서비스를 이용해 entity를 업데이트
        List<TodoEntity> entities = service.update(entity);

        // 4 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 5 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // 6 ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        try {
            // 1 TodoEntity로 변환한다
            TodoEntity entity = TodoDTO.toEntity(dto);

            // 2 임시 사용자 아이디를 설정
            entity.setUserId(userId);

            // 3 서비스를 이용해 user 삭제
            List<TodoEntity> entities = service.remove(entity);

            // 4 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 5 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // 6 ResponseDTO를 리턴
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 7 예외가 있는 경우 dto 대신 error에 메시지를 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
