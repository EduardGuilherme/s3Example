package com.aula.s3.s3exemplo.sqs.controller;

import com.aula.s3.s3exemplo.sqs.entity.CepEntity;
import com.aula.s3.s3exemplo.sqs.service.CepRdsService;
import com.aula.s3.s3exemplo.sqs.service.CepSqsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cep")
public class CepController {
    private final CepSqsService cepSqsService;
    private final CepRdsService cepRdsService;

    public CepController(CepSqsService cepSqsService, CepRdsService cepRdsService) {
        this.cepSqsService = cepSqsService;
        this.cepRdsService = cepRdsService;
    }

    // Envia CEP para a fila SQS — retorna o ID do procedimento
    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestParam String cep) {
        return ResponseEntity.ok(cepSqsService.sendCep(cep));
    }

    // Consome fila, consulta ViaCEP e salva no RDS
    @PostMapping("/process")
    public ResponseEntity<List<Map<String, String>>> process() {
        return ResponseEntity.ok(cepSqsService.receiveAndProcess());
    }

    // Lista todos os CEPs salvos no RDS
    @GetMapping("/list")
    public ResponseEntity<List<CepEntity>> list() {
        return ResponseEntity.ok(cepRdsService.listAll());
    }
}
