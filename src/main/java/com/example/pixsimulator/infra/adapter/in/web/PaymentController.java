package com.example.pixsimulator.infra.adapter.in.web;

import com.example.pixsimulator.application.port.in.ConfirmPaymentUseCase;
import com.example.pixsimulator.application.port.in.CreatePaymentUseCase;
import com.example.pixsimulator.domain.Payment;
import com.example.pixsimulator.infra.adapter.in.presenter.PaymentWebPresenter;
import com.example.pixsimulator.infra.rest.api.PaymentsApi;
import com.example.pixsimulator.infra.rest.model.ConfirmPaymentResponse;
import com.example.pixsimulator.infra.rest.model.CreatePaymentRequest;
import com.example.pixsimulator.infra.rest.model.CreatePaymentResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PaymentController implements PaymentsApi {

    private final CreatePaymentUseCase createPaymentUseCase;

    private final ConfirmPaymentUseCase confirmPaymentUseCase;

    private final PaymentWebPresenter presenter;

    @Override
    public ResponseEntity<ConfirmPaymentResponse> confirmPayment(   Long id) {
        Payment payment = confirmPaymentUseCase.confirm(id);
        return presenter.presentConfirmation(payment);
    }

    @Override
    public ResponseEntity<CreatePaymentResponse> createPayment(CreatePaymentRequest createPaymentRequest) {
        Payment response = this.createPaymentUseCase.create(createPaymentRequest.getPixKey(),
                                                            createPaymentRequest.getAmount(),
                                                            createPaymentRequest.getWebhookUrl());

        return presenter.presentCreation(response);
    }
}
