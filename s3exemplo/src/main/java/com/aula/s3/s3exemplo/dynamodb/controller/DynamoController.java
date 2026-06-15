package com.aula.s3.s3exemplo.dynamodb.controller;

import com.aula.s3.s3exemplo.dynamodb.service.DynamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dynamodb")
public class DynamoController {
    private final DynamoService dynamoService;

    public DynamoController(DynamoService service){
        this.dynamoService = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(
            @RequestParam String id,
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(dynamoService.save(id, nome));
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Map<String, String>> find(@PathVariable String id) {
        return ResponseEntity.ok(dynamoService.find(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(dynamoService.delete(id));
    }
}
