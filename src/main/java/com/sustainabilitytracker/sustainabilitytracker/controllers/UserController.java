package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.RegisterUserRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.UserResponse;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.mappers.UserMapper;
import com.sustainabilitytracker.sustainabilitytracker.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
//    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        System.out.println("CLICKED!!");
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(
            UriComponentsBuilder uriBuilder,
            @RequestBody RegisterUserRequest request){
        System.out.println("=========================");
        if(userRepository.existsByEmail(request.getEmail())) return ResponseEntity.badRequest().build();
        var user = userMapper.toEntity(request);
        user.setCompany(null);
        user.setDepartment(null);
        user.setRole(Role.ADMIN);
        user.setPassword(user.getPassword());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        var userResponse = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userResponse.getId()).toUri();

        return ResponseEntity.created(uri).body(userResponse);
    }
}
