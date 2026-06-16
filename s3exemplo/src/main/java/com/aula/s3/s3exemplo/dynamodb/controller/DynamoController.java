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
            @RequestParam String personid,
            @RequestParam String keyranking,
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(dynamoService.save(personid, keyranking, nome));
    }
    @GetMapping("/find")
    public ResponseEntity<Map<String, String>> find(@RequestParam String personid,  @RequestParam String keyranking) {
        return ResponseEntity.ok(dynamoService.find(personid, keyranking));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestParam String personid,
            @RequestParam String keyranking
    ) {
        return ResponseEntity.ok(dynamoService.delete(personid, keyranking));
    }
}
