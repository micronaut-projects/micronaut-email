package io.micronaut.email.docs;

import io.micronaut.email.BodyType;
import io.micronaut.email.EmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.MultipartBody;
import jakarta.inject.Singleton;

@Singleton
public class WelcomeService {
    private final EmailSender<?, ?> emailSender;

    public WelcomeService(EmailSender<?, ?> emailSender) {
        this.emailSender = emailSender;
    }

    public void sendWelcomeEmail() {
        emailSender.send(Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .body(new MultipartBody("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>", "Hello dear Micronaut user")));
    }
}
