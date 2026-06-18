package com.aula.s3.s3exemplo.sqs.service;

import com.aula.s3.s3exemplo.sqs.entity.CepEntity;
import com.aula.s3.s3exemplo.sqs.repository.CepRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CepRdsService {
    private final CepRepository cepRepository;

    public CepRdsService(CepRepository cepRepository) {
        this.cepRepository = cepRepository;
    }

    public CepEntity save(String cep, String logradouro) {
        CepEntity entity = new CepEntity();
        entity.setCep(cep);
        entity.setLogradouro(logradouro);
        return cepRepository.save(entity);
    }

    public List<CepEntity> listAll() {
        return cepRepository.findAll();
    }
}
