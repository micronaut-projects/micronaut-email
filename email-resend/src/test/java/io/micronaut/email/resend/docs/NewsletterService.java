package io.micronaut.email.resend.docs;

import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import io.micronaut.email.resend.model.ResendEmailRequest;
import io.micronaut.email.resend.model.ResendEmailResponse;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

//tag::clazz[]
@Singleton
public class NewsletterService {

    private final EmailSender<ResendEmailRequest.Builder, ResendEmailResponse> emailSender;

    public NewsletterService(EmailSender<ResendEmailRequest.Builder, ResendEmailResponse> emailSender) {
        this.emailSender = emailSender;
    }

    public String sendNewsletter(String recipient, String issue) {
        Email.Builder email = Email.builder()
            .from("newsletter@example.com")
            .to(recipient)
            .subject("Newsletter " + issue)
            .body("<strong>Hello</strong> dear Micronaut user.", "Hello dear Micronaut user.");

        ResendEmailResponse response = emailSender.send(email, request -> request
            .tag("category", "newsletter")
            .header("List-Unsubscribe", "<mailto:unsubscribe@example.com>")
            .scheduledAt(Instant.now().plus(1, ChronoUnit.HOURS))
            .idempotencyKey("newsletter-" + issue + "-" + recipient));
        return response.id();
    }
}
//end::clazz[]
