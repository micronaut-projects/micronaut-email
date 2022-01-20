package io.micronaut.email.docs;

import io.micronaut.email.BodyType;
import io.micronaut.email.EmailSender;
import io.micronaut.email.Email;
import jakarta.inject.Singleton;

@Singleton
public class WelcomeService {
    private final EmailSender<?, ?> emailSender;

    public WelcomeService(EmailSender<?, ?> emailSender) {
        this.emailSender = emailSender;
    }

    public void sendWelcomeEmailText() {
        emailSender.send(Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .body("Hello dear Micronaut user"));
    }

    public void sendWelcomeEmailHtml() {
        emailSender.send(Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .body("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>", BodyType.HTML));
    }
}
