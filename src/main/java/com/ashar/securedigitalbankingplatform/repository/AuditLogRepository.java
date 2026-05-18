package com.ashar.securedigitalbankingplatform.repository;

import com.ashar.securedigitalbankingplatform.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}