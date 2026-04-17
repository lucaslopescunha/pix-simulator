package com.example.pixsimulator.infra.adapter.out.webclient;

import com.example.pixsimulator.application.port.out.PaymentRepositoryPort;
import com.example.pixsimulator.domain.Payment;
import com.example.pixsimulator.domain.PaymentStatusQuery;
import com.example.pixsimulator.domain.enumeration.PaymentStatus;
import com.example.pixsimulator.infra.rest.model.CreatePaymentRequest;
import com.example.pixsimulator.infra.rest.model.WebhookRequest;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class WebhookScheduler {

    private final PaymentRepositoryPort repositoryPort;
    private final WebClient webClient;
    private final String defaultUrl;

    public WebhookScheduler(PaymentRepositoryPort repositoryPort,
            WebClient webClient,
            @Value("${pix.webhook.default-url}") String defaultUrl) {
        this.repositoryPort = repositoryPort;
        this.webClient = webClient;
        this.defaultUrl = defaultUrl;
    }

    /**
     *  Por que usar WebClient mesmo em projetos "não-reativos"?
     * Mesmo que você não use Kafka ou WebFlux no projeto todo, o WebClient é preferível porque:
     * É muito mais fácil de testar (com MockWebServer).
     * O suporte a timeouts e filtros é superior.
     * O RestTemplate entrou em modo de manutenção (não receberá novos recursos).
     * Dica para o seu caso (Webhooks):
     * Ao usar o WebClient para Webhooks, sempre configure um Timeout. Se o servidor do seu cliente estiver "pendurado",
     * sem o timeout seu Scheduler pode ficar travado para sempre esperando uma resposta que nunca vem.
     */
    @Scheduled(fixedDelayString = "${pix.webhook.schedule-ms:5000}")
    public void deliverPendingWebooks() {
        List<Payment> pending =
                repositoryPort.findByStatusAndWebhookSentFalse(new PaymentStatusQuery(PaymentStatus.CONFIRMED));
        for(Payment p: pending) {
            String url = StringUtils.isNotBlank(p.getWebhookUrl()) ? p.getWebhookUrl() : defaultUrl;
            try {
//                webClient.post()
//                        .uri(url)
//                        .bodyValue(new WebhookRequest(p.getId(), p.getAmount()
//                        )).retrieve()
//                        .bodyToMono(Void.class)
//                        .timeout(Duration.ofSeconds(5))
//                        .onErrorResume(e-> Mono.empty())
//                        .block();

                repositoryPort.markWebhookSent(p.getId());
            } catch(Exception e) {
                log.error("Erro ao entregar o webhook {}", e.getMessage());
            }
        }
    }

}
