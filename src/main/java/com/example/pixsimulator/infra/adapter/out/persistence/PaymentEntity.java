package com.example.pixsimulator.infra.adapter.out.persistence;

import com.example.pixsimulator.domain.enumeration.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pixKey;

    private BigDecimal amount;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private String webhookUrl;
    private boolean webhookSent;


}
