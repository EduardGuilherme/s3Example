package com.aula.s3.s3exemplo.rds.controller;

import com.aula.s3.s3exemplo.rds.entity.Users;
import com.aula.s3.s3exemplo.rds.repository.userRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/rds")
public class UserController {

    private final userRepository  respository;

    @PostMapping("/save")
    public ResponseEntity<Users> save(@RequestBody Users users){
        return ResponseEntity.ok(respository.save(users));
    }

    @GetMapping("/list")
    public List<Users> find(){
        return respository.findAll();
    }
}
