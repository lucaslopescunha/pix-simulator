package com.example.pixsimulator.application.service;

import com.example.pixsimulator.application.port.in.CreatePaymentUseCase;
import com.example.pixsimulator.application.port.out.PaymentRepositoryPort;
import com.example.pixsimulator.domain.Payment;
import com.example.pixsimulator.domain.enumeration.PaymentStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@AllArgsConstructor
public class CreatePaymentService implements CreatePaymentUseCase {

    private final PaymentRepositoryPort repositoryPort;


    @Override
    public Payment create(String pixKey, BigDecimal amount, String webhookUrl) {
        Payment p =
                Payment.builder()
                       .pixKey(pixKey)
                       .amount(amount)
                       .status(PaymentStatus.CREATED)
                       .createdAt(Instant.now())
                       .webhookUrl(webhookUrl)
                       .webhookSent(false)
                       .build();
        return this.repositoryPort.save(p);
    }
}
