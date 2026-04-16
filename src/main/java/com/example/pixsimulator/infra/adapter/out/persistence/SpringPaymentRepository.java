package com.example.pixsimulator.infra.adapter.out.persistence;

import com.example.pixsimulator.domain.enumeration.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByStatusAndWebhookSentFalse(PaymentStatus status);
}
