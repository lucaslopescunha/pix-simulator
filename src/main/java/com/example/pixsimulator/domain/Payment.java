package com.example.pixsimulator.domain;

import com.example.pixsimulator.domain.enumeration.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private Long id;
    private String pixKey;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private String webhookUrl;
    private boolean webhookSent;


}
