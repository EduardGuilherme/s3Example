package com.aula.s3.s3exemplo.dynamodb.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;


import java.util.HashMap;
import java.util.Map;

@Service
public class DynamoService {
    private final DynamoDbClient dynamoDbClient;

    private static final String TABLE_NAME = "aws-db-java";

    public DynamoService(DynamoDbClient dynamoDbClient){
        this.dynamoDbClient = dynamoDbClient;
    }

    public String save(String id, String nome, String key){
        Map<String, AttributeValue> item = new HashMap<>();

        item.put("personId", AttributeValue.builder().s(id).build());
        item.put("keyranking", AttributeValue.builder().s(key).build());
        item.put("nome", AttributeValue.builder().s(nome).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);

        return "Item salvo com sucesso";
    }
    public Map<String, String> find(String id,String keyopt) {

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("personId", AttributeValue.builder().s(id).build());
        key.put("keyranking", AttributeValue.builder().s(keyopt).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();

        // Converte AttributeValue -> String (assume que todos os campos são strings)
        Map<String, String> result = new HashMap<>();
        item.forEach((k, v) -> {
            result.put(k, v.s());
        });

        result.put("all-data",item.toString());

        return result;
    }
    public String delete(String id,String keyOpt) {

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("personId", AttributeValue.builder().s(id).build());
        key.put("keyranking", AttributeValue.builder().s(keyOpt).build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        dynamoDbClient.deleteItem(request);

        return "Item removido com sucesso";
    }
}
