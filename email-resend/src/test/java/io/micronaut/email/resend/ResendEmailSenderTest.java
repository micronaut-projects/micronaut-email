package io.micronaut.email.resend;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.type.Argument;
import io.micronaut.email.AsyncEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.EmailSender;
import io.micronaut.email.resend.docs.NewsletterService;
import io.micronaut.email.resend.model.ResendEmailRequest;
import io.micronaut.email.resend.model.ResendEmailResponse;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.json.JsonMapper;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResendEmailSenderTest implements TestPropertyProvider {
    private static final String MOCK_SPEC_NAME = "ResendEmailSenderTest.ResendMock";
    private static final String EMAIL_ID = "49a3999c-0ce1-4ea6-ab68-afcd6dc2e794";
    private static final String SUBJECT_INVALID = "invalid";
    private static final String SUBJECT_NOT_FOUND = "not found";
    private static final String SUBJECT_BAD_GATEWAY = "bad gateway";

    private EmbeddedServer resend;

    @Inject
    EmailSender<ResendEmailRequest.Builder, ResendEmailResponse> emailSender;

    @Inject
    AsyncEmailSender<ResendEmailRequest.Builder, ResendEmailResponse> asyncEmailSender;

    @Inject
    JsonMapper jsonMapper;

    @Override
    public Map<String, String> getProperties() {
        resend = ApplicationContext.run(EmbeddedServer.class, Map.of("spec.name", MOCK_SPEC_NAME));
        return Map.of(
            "micronaut.http.services.resend.url", resend.getURL().toString(),
            "resend.api-key", "re_test"
        );
    }

    @AfterAll
    void stopResend() {
        if (resend != null) {
            resend.close();
        }
    }

    @Test
    void sendsEmail() throws IOException {
        ResendEmailResponse response = emailSender.send(email("Hello"));

        assertEquals(EMAIL_ID, response.id());
        assertRequest("Hello");
    }

    @Test
    void sendsEmailAsynchronously() throws IOException {
        ResendEmailResponse response = Mono.from(asyncEmailSender.sendAsync(email("Hello async"))).block();

        assertNotNull(response);
        assertEquals(EMAIL_ID, response.id());
        assertRequest("Hello async");
    }

    @Test
    void consumerCustomizesTheRequest(NewsletterService newsletterService) throws IOException {
        assertEquals(EMAIL_ID, newsletterService.sendNewsletter("receiver@example.com", "42"));

        ResendMock mock = mock();
        assertEquals("newsletter-42-receiver@example.com", mock.idempotencyKey);
        Map<String, Object> json = json(mock);
        assertEquals("Newsletter 42", json.get("subject"));
        assertEquals(List.of(Map.of("name", "category", "value", "newsletter")), json.get("tags"));
        assertEquals(Map.of("List-Unsubscribe", "<mailto:unsubscribe@example.com>"), json.get("headers"));
        assertNotNull(json.get("scheduled_at"));
        assertEquals("<strong>Hello</strong> dear Micronaut user.", json.get("html"));
        assertEquals("Hello dear Micronaut user.", json.get("text"));
    }

    @Test
    void resendErrorIsThrownAsResendEmailException() {
        ResendEmailException e = assertThrows(ResendEmailException.class, () -> emailSender.send(email(SUBJECT_INVALID)));

        assertValidationError(e);
    }

    @Test
    void resendErrorIsEmittedAsResendEmailException() {
        Mono<ResendEmailResponse> response = Mono.from(asyncEmailSender.sendAsync(email(SUBJECT_INVALID)));

        ResendEmailException e = assertThrows(ResendEmailException.class, response::block);

        assertValidationError(e);
    }

    @Test
    void errorWithoutResendErrorBody() {
        ResendEmailException e = assertThrows(ResendEmailException.class, () -> emailSender.send(email(SUBJECT_BAD_GATEWAY)));

        assertEquals(502, e.getStatusCode());
        assertNull(e.getError());
        assertEquals("Resend responded with HTTP 502", e.getMessage());
    }

    @Test
    void notFoundIsThrownAsEmailException() {
        assertThrows(EmailException.class, () -> emailSender.send(email(SUBJECT_NOT_FOUND)));

        Mono<ResendEmailResponse> response = Mono.from(asyncEmailSender.sendAsync(email(SUBJECT_NOT_FOUND)));
        assertThrows(EmailException.class, response::block);
    }

    private void assertRequest(String subject) throws IOException {
        ResendMock mock = mock();
        assertEquals("Bearer re_test", mock.authorization);
        assertEquals("micronaut-email-resend", mock.userAgent);
        assertNull(mock.idempotencyKey);
        assertTrue(mock.contentType.startsWith(MediaType.APPLICATION_JSON));
        assertEquals(Map.of(
            "from", "sender@example.com",
            "to", List.of("receiver@example.com"),
            "subject", subject,
            "text", "Text body"
        ), json(mock));
    }

    private static void assertValidationError(ResendEmailException e) {
        assertEquals(422, e.getStatusCode());
        assertNotNull(e.getError());
        assertEquals("validation_error", e.getError().name());
        assertEquals("Invalid `from` field.", e.getError().message());
        assertEquals("Resend responded with HTTP 422 (validation_error): Invalid `from` field.", e.getMessage());
        assertNotNull(e.getCause());
        assertFalse(assertInstanceOf(EmailException.class, e).getMessage().isEmpty());
    }

    private ResendMock mock() {
        return resend.getApplicationContext().getBean(ResendMock.class);
    }

    private Map<String, Object> json(ResendMock mock) throws IOException {
        return jsonMapper.readValue(mock.body, Argument.mapOf(String.class, Object.class));
    }

    private static Email.Builder email(String subject) {
        return Email.builder()
            .from("sender@example.com")
            .to("receiver@example.com")
            .subject(subject)
            .body("Text body");
    }

    @Requires(property = "spec.name", value = MOCK_SPEC_NAME)
    @Singleton
    static class ResendMock {
        String authorization;
        String userAgent;
        String idempotencyKey;
        String contentType;
        String body;
    }

    @Requires(property = "spec.name", value = MOCK_SPEC_NAME)
    @Controller("/emails")
    static class ResendMockController {
        private final ResendMock mock;

        ResendMockController(ResendMock mock) {
            this.mock = mock;
        }

        @Post
        HttpResponse<?> send(HttpRequest<?> request, @Body String body) {
            mock.authorization = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
            mock.userAgent = request.getHeaders().get(HttpHeaders.USER_AGENT);
            mock.idempotencyKey = request.getHeaders().get("Idempotency-Key");
            mock.contentType = request.getHeaders().get(HttpHeaders.CONTENT_TYPE);
            mock.body = body;
            if (body.contains('"' + SUBJECT_INVALID + '"')) {
                return HttpResponse.unprocessableEntity()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"statusCode\":422,\"name\":\"validation_error\",\"message\":\"Invalid `from` field.\"}");
            }
            if (body.contains('"' + SUBJECT_NOT_FOUND + '"')) {
                return HttpResponse.notFound();
            }
            if (body.contains('"' + SUBJECT_BAD_GATEWAY + '"')) {
                return HttpResponse.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<html><body>Bad Gateway</body></html>");
            }
            return HttpResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"id\":\"" + EMAIL_ID + "\"}");
        }
    }
}
