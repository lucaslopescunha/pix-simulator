package com.example.pixsimulator.infra.adapter.in.presenter;

import com.example.pixsimulator.domain.Payment;
import com.example.pixsimulator.infra.rest.model.ConfirmPaymentResponse;
import com.example.pixsimulator.infra.rest.model.CreatePaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class PaymentWebPresenter {

    public ResponseEntity<CreatePaymentResponse> presentCreation(Payment payment) {
        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setId(payment.getId());
        response.setStatus(payment.getStatus().name());
        return ResponseEntity.created(URI.create("/api/v1/payments/" + payment.getId())).body(response);
    }

    public ResponseEntity<ConfirmPaymentResponse> presentConfirmation(Payment payment) {
        ConfirmPaymentResponse response = new ConfirmPaymentResponse();
        response.setId(payment.getId());
        response.setStatus(payment.getStatus().name());
        return ResponseEntity.status(HttpStatus.OK ).body(response);
    }



}
