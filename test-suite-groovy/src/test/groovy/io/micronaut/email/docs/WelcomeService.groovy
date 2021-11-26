package io.micronaut.email.docs

import io.micronaut.email.EmailCourier
import io.micronaut.email.TransactionalEmail
import jakarta.inject.Singleton

@Singleton
class WelcomeService {
    private final EmailCourier emailCourier

    WelcomeService(EmailCourier emailCourier) {
        this.emailCourier = emailCourier
    }

    void sendWelcomeEmail() {
        emailCourier.send(TransactionalEmail.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .text("Hello dear Micronaut user")
                .html("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>")
                .build())
    }
}
