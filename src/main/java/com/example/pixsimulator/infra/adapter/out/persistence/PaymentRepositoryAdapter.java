package com.example.pixsimulator.infra.adapter.out.persistence;

import com.example.pixsimulator.application.port.out.PaymentRepositoryPort;
import com.example.pixsimulator.domain.Payment;
import com.example.pixsimulator.domain.PaymentStatusQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {
    private final SpringPaymentRepository paymentRepositoryPort;


    @Override
    public Payment save(Payment payment) {
         PaymentEntity entity = paymentRepositoryPort.save(toEntity(payment));
         return toDomain(entity);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepositoryPort.findById(id).map(this::toDomain);
    }

    @Override
    public List<Payment> findByStatusAndWebhookSentFalse(PaymentStatusQuery query) {
        return paymentRepositoryPort.findByStatusAndWebhookSentFalse(query.status())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());

    }

    @Override
    public void markWebhookSent(Long id) {
        paymentRepositoryPort.findById(id).ifPresent( payment -> {
            payment.setWebhookSent(true);
            paymentRepositoryPort.save(payment);
        });
    }

    public Payment toDomain(PaymentEntity entity) {
        Payment payment = Payment.builder()
                .id(entity.getId())
                .pixKey(entity.getPixKey())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .confirmedAt(entity.getConfirmedAt())
                .webhookUrl(entity.getWebhookUrl())
                .webhookSent(entity.isWebhookSent())
                .build();
        return payment;
    }

    private PaymentEntity toEntity(Payment p) {
        PaymentEntity entity = PaymentEntity.builder()
                .id(p.getId())
                .pixKey(p.getPixKey())
                .amount(p.getAmount())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .confirmedAt(p.getConfirmedAt())
                .webhookUrl(p.getWebhookUrl())
                .webhookSent(p.isWebhookSent())
                .build();
        return entity;
    }
}
