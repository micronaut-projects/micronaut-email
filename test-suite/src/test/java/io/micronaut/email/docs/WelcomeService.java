package io.micronaut.email.docs;

import io.micronaut.email.EmailSender;
import io.micronaut.email.Email;
import jakarta.inject.Singleton;

@Singleton
public class WelcomeService {
    private final EmailSender emailSender;

    public WelcomeService(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendWelcomeEmail() {
        emailSender.send(Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .text("Hello dear Micronaut user")
                .html("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>")
                .build());
    }
}
