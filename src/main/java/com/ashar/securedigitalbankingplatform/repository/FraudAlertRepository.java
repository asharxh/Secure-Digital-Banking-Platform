package com.ashar.securedigitalbankingplatform.repository;

import com.ashar.securedigitalbankingplatform.entity.FraudAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
}