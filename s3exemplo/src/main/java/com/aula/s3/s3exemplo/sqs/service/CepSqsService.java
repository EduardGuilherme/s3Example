package com.aula.s3.s3exemplo.sqs.service;

import com.aula.s3.s3exemplo.sqs.dto.ViaCepResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CepSqsService {
    private final SqsClient sqsClient;
    private final CepRdsService cepRdsService;
    private final RestClient restClient;

    private static final String QUEUE_URL =
            "https://sqs.us-east-2.amazonaws.com/566112927423/myQueueJava";

    public CepSqsService(SqsClient sqsClient, CepRdsService cepRdsService) {
        this.sqsClient = sqsClient;
        this.cepRdsService = cepRdsService;
        this.restClient = RestClient.create();
    }

    // Envia o CEP para a fila e retorna o ID do procedimento
    public String sendCep(String cep) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .messageBody(cep)
                .build();

        SendMessageResponse response = sqsClient.sendMessage(request);
        return "Mensagem enviada com sucesso. ID: " + response.messageId();
    }

    // Consome mensagens da fila, consulta ViaCEP e salva no RDS
    public List<Map<String, String>> receiveAndProcess() {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(2)
                .visibilityTimeout(30)
                .build();

        List<Message> messages = sqsClient.receiveMessage(request).messages();
        List<Map<String, String>> processed = new ArrayList<>();

        for (Message message : messages) {
            String cep = message.body();
            try {
                // Consulta ViaCEP
                ViaCepResponse dados = restClient.get()
                        .uri("https://viacep.com.br/ws/{cep}/json/", cep)
                        .retrieve()
                        .body(ViaCepResponse.class);

                // Salva no RDS
                cepRdsService.save(dados.cep(), dados.logradouro());

                // Deleta da fila
                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(QUEUE_URL)
                        .receiptHandle(message.receiptHandle())
                        .build());

                processed.add(Map.of(
                        "messageId", message.messageId(),
                        "cep", dados.cep(),
                        "logradouro", dados.logradouro(),
                        "status", "PROCESSADA E SALVA"
                ));
            } catch (Exception e) {
                processed.add(Map.of(
                        "messageId", message.messageId(),
                        "cep", cep,
                        "status", "FALHA - SERÁ REPROCESSADA",
                        "erro", e.getMessage()
                ));
            }
        }

        return processed;
    }
}
