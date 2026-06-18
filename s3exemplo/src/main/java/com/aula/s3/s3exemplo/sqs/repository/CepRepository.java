package com.aula.s3.s3exemplo.sqs.repository;

import com.aula.s3.s3exemplo.sqs.entity.CepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CepRepository extends JpaRepository<CepEntity, Long> {
}
