package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.TransferEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "transfer-events";

    public void publishTransferEvent(
            TransferEventDTO event
    ) {

        kafkaTemplate.send(TOPIC, event);

        System.out.println(
                "TRANSFER EVENT PUBLISHED TO KAFKA"
        );
    }
}