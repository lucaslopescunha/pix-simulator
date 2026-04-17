package com.example.pixsimulator.application.service;

import com.example.pixsimulator.application.port.out.PaymentRepositoryPort;
import com.example.pixsimulator.domain.Payment;
import com.example.pixsimulator.domain.enumeration.PaymentStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfirmPaymentServiceTest {

    @Mock
    private PaymentRepositoryPort repositoryPort;

    @Mock
    private Clock clock;

    @InjectMocks
    private ConfirmPaymentService service;

    @Test
    @DisplayName("Should confirm when payment is found")
    public void shouldConfirm_When_PaymentIsFound() {
        long id = 1l;

        LocalDateTime fixedTime = LocalDateTime.of(2026, Month.APRIL, 8, 0, 0);
        ZoneId zoneId = ZoneId.systemDefault();

        Payment paymentFound = createPayment(id,
                PaymentStatus.CREATED,
                fixedTime,
                null
        );

        Payment paymentUpdated = createPayment(id,
                PaymentStatus.CREATED,
                fixedTime,
                fixedTime
        );

        Payment paymentConfirmed = createPayment(id,
                PaymentStatus.CONFIRMED,
                fixedTime,
                fixedTime
        );

        Payment expected = createPayment(id,
                PaymentStatus.CONFIRMED,
                fixedTime,
                fixedTime
        );


        when(clock.getZone()).thenReturn(zoneId);
        when(clock.instant()).thenReturn(fixedTime.atZone(zoneId).toInstant());
        when(repositoryPort.findById(id)).thenReturn(Optional.of(paymentFound));
        when(repositoryPort.save(paymentUpdated)).thenReturn(paymentConfirmed);

        assertEquals(expected, service.confirm(1L));

        verify(clock, times(1)).getZone();
        verify(clock, times(1)).instant();
        verify(repositoryPort, times(1)).findById(1l);
        verify(repositoryPort, times(1)).save(paymentConfirmed);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(clock);
        verifyNoMoreInteractions(repositoryPort);
    }

    private Payment createPayment(Long id, PaymentStatus status, LocalDateTime createdAt, LocalDateTime confirmedAt) {
        return Payment.builder()
                .id(id)
                .status(status)
                .amount(BigDecimal.ONE)
                .createdAt(createdAt)
                .confirmedAt(confirmedAt)
                .pixKey("pixKey")
                .webhookSent(false)
                .webhookUrl("webhookUrl1")
                .build();

    }

}
