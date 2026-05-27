package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.TransferEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditConsumerService {

    @KafkaListener(topics = "transfer-events", groupId = "audit-group")
    public void consume(TransferEventDTO event){
        System.out.println("AUDIT LOG CREATED");
    }
}