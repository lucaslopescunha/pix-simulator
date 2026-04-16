package com.example.pixsimulator.application.port.out;

import com.example.pixsimulator.domain.Payment;
import com.example.pixsimulator.domain.PaymentStatusQuery;
import com.example.pixsimulator.domain.enumeration.PaymentStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryPort {

    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    List<Payment> findByStatusAndWebhookSentFalse(PaymentStatusQuery query);
    void markWebhookSent(Long id);

}
