package com.example.pixsimulator.application.service;

import com.example.pixsimulator.application.port.in.ConfirmPaymentUseCase;
import com.example.pixsimulator.application.port.out.PaymentRepositoryPort;
import com.example.pixsimulator.domain.Payment;
import com.example.pixsimulator.domain.enumeration.PaymentStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class ConfirmPaymentService implements ConfirmPaymentUseCase {
    private final PaymentRepositoryPort repositoryPort;

    @Override
    public Payment confirm(Long id) {
        var opt = repositoryPort.findById(id);
        if(opt.isEmpty()) {
            throw new IllegalArgumentException("Payment not found %d ".formatted(id));
        }
        Payment p = opt.get();
        p.setStatus(PaymentStatus.CONFIRMED);
        p.setConfirmedAt(Instant.now());
        p.setWebhookSent(false);
        return repositoryPort.save(p);
    }
}
