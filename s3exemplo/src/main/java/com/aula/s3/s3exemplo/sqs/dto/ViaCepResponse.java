package com.aula.s3.s3exemplo.sqs.dto;

public record ViaCepResponse(String cep, String logradouro, String bairro, String localidade, String uf) {
}
