package io.micronaut.email.resend;

import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import io.micronaut.email.resend.model.ResendEmailRequest;
import io.micronaut.email.resend.model.ResendEmailResponse;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Sends an email with Resend. The environment variable {@code RESEND_API_KEY} populates the property {@code resend.api-key}.
 */
@EnabledIfEnvironmentVariable(
    named = "RESEND_API_KEY",
    matches = ".+"
)
@MicronautTest(startApplication = false)
class ResendFunctionalTest {
    private static final String SENDER_EMAIL = env("RESEND_FROM", "onboarding@resend.dev");
    private static final String RECIPIENT_EMAIL = env("RESEND_TO", "delivered@resend.dev");

    @Test
    void testSendMail(EmailSender<ResendEmailRequest.Builder, ResendEmailResponse> emailSender) {
        Email.Builder builder = Email.builder()
            .subject("Micronaut Email Resend integration")
            .from(SENDER_EMAIL)
            .to(RECIPIENT_EMAIL)
            .body("<strong>Hello</strong> from Micronaut Email.", "Hello from Micronaut Email.");

        ResendEmailResponse response = emailSender.send(builder, request -> request.tag("category", "functional-test"));

        assertNotNull(response.id());
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
