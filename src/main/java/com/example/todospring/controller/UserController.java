package com.example.todospring.controller;

import com.example.todospring.dto.ResponseDTO;
import com.example.todospring.dto.UserDTO;
import com.example.todospring.model.UserEntity;
import com.example.todospring.security.TokenProvider;
import com.example.todospring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
//@CrossOrigin(origins="http://localhost:3000", maxAge=3600)
@CrossOrigin(origins= {"http://localhost:3000", "http://ec2-15-164-50-0.ap-northeast-2.compute.amazonaws.com"}, maxAge=3600)
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    //@Autowired
    //private BCryptPasswordEncoder passwordEncoder;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        log.info("/signup registerUser executed");
        try {
            // Request를 이용해 저장할 사용자 만들기
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    //.password(userDTO.getPassword())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();
            // 서비스를 이용해 리포지터리에 사용자 저장
            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    //.password(registeredUser.getPassword())
                    .password(passwordEncoder.encode(registeredUser.getPassword()))
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            // 사용자 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }

    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        log.info("/signin authenticate executed..");
        UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

        if(user!= null) {
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getUsername())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


}
