package io.micronaut.email.mailpit.client;

import io.micronaut.context.annotation.Requires;
import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;

@Requires(property = "spec.name", value = "OrderServiceTest")
@Singleton
public class OrderService {
    private final EmailSender<?, ?> emailSender;

    public OrderService(EmailSender<?, ?> emailSender) {
        this.emailSender = emailSender;
    }

    public void sendOrderEmail(@jakarta.validation.constraints.Email String recipient,
                               @NotBlank String orderNumber) {
        String text = "We have received your order " + orderNumber + ". You will receive your product soon.";
        String html = "<html><body><p>" + text + "</p></body></html>";
        emailSender.send(Email.builder()
                .to(recipient)
                .subject("Order Number: " + orderNumber)
                .body(html, text));
    }
}
