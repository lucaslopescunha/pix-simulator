package com.example.pixsimulator.domain;

import com.example.pixsimulator.domain.enumeration.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private Long id;
    private String pixKey;
    private BigDecimal amount;
    private PaymentStatus status;
    private Instant createdAt;
    private Instant confirmedAt;
    private String webhookUrl;
    private boolean webhookSent;


}
