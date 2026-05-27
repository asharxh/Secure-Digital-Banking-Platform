package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.TransferEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailConsumerService {

    @KafkaListener(topics = "transfer-events", groupId = "email-group")
    public void consume(TransferEventDTO event){
        System.out.println("EMAIL SENT FOR TRANSFER");
    }
}