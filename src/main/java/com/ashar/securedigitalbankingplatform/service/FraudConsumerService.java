package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.TransferEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudConsumerService {

    @KafkaListener(topics = "transfer-events", groupId = "fraud-group")
    public void consume(TransferEventDTO event){
        System.out.println("FRAUD CHECK FOR TRANSFER: " + event.getAmount());

        if (event.getAmount() > 20000) {
            System.out.println("POSSIBLE FRAUD DETECTED");
        }
    }
}