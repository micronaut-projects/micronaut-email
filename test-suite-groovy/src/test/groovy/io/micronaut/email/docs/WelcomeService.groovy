package io.micronaut.email.docs


import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import jakarta.inject.Singleton

@Singleton
class WelcomeService {
    private final EmailSender emailCourier

    WelcomeService(EmailSender emailCourier) {
        this.emailCourier = emailCourier
    }

    void sendWelcomeEmail() {
        emailCourier.send(Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .text("Hello dear Micronaut user")
                .html("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>")
                .build())
    }
}
