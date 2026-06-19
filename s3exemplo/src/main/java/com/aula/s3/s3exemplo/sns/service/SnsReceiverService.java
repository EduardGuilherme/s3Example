package com.aula.s3.s3exemplo.sns.service;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ConfirmSubscriptionRequest;
import tools.jackson.databind.JsonNode;


@Service
public class SnsReceiverService {
    private static final Logger log = LoggerFactory.getLogger(SnsReceiverService.class);

    private final SnsClient snsClient;

    public SnsReceiverService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public String confirmSubscription(JsonNode payload) {

        String topicArn = payload.get("TopicArn").asText();
        String token = payload.get("Token").asText();

        snsClient.confirmSubscription(ConfirmSubscriptionRequest.builder()
                .topicArn(topicArn)
                .token(token)
                .build());

        log.info("Inscrição confirmada automaticamente para topic: {}", topicArn);

        return "Subscription confirmed";
    }


    public String processNotification(JsonNode payload) {

        log.info(" ");
        log.info("Novidade recebida do SNS!");
        log.info("Message ID: {}", payload.toString());
        log.info(" ");

        return "Notification processed: ";
    }
}
