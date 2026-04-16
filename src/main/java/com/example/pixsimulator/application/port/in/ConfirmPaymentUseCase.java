package com.example.pixsimulator.application.port.in;

import com.example.pixsimulator.domain.Payment;

public interface ConfirmPaymentUseCase {

    Payment confirm(Long id);
}
