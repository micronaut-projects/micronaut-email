package io.micronaut.email.docs;

import io.micronaut.email.EmailCourier;
import io.micronaut.email.TransactionalEmail;
import jakarta.inject.Singleton;

@Singleton
public class WelcomeService {
    private final EmailCourier emailCourier;

    public WelcomeService(EmailCourier emailCourier) {
        this.emailCourier = emailCourier;
    }

    public void sendWelcomeEmail() {
        emailCourier.send(TransactionalEmail.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .text("Hello dear Micronaut user")
                .html("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>")
                .build());
    }
}
