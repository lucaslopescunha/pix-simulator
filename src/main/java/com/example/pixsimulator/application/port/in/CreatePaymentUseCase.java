package com.example.pixsimulator.application.port.in;

import com.example.pixsimulator.domain.Payment;

import java.math.BigDecimal;

public interface CreatePaymentUseCase {

    Payment create(String pixKey, BigDecimal amount, String webhookUrl);
}
