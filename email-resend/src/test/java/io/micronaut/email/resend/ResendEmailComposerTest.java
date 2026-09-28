package io.micronaut.email.resend;

import io.micronaut.context.annotation.Property;
import io.micronaut.email.Attachment;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.resend.model.ResendAttachment;
import io.micronaut.email.resend.model.ResendEmailRequest;
import io.micronaut.email.resend.model.ResendTag;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Property(name = "resend.api-key", value = "re_xxx")
@MicronautTest(startApplication = false)
class ResendEmailComposerTest {
    private static final byte[] CONTENT = "attachment content".getBytes(StandardCharsets.UTF_8);

    @Test
    void emailIsMappedToResendEmailRequest(ResendEmailComposer composer) {
        Email email = Email.builder()
            .from(new Contact("sender@example.com", "Sender"))
            .to(new Contact("receiver@example.com", "Receiver"))
            .to("other@example.com")
            .cc("cc@example.com")
            .bcc(new Contact("bcc@example.com", "Blind"))
            .replyTo("support@example.com")
            .replyTo(new Contact("sales@example.com", "Sales"))
            .subject("Hello")
            .body("<strong>HTML body</strong>", "Text body")
            .attachment(Attachment.builder()
                .filename("note.txt")
                .contentType("text/plain")
                .content(CONTENT)
                .build())
            .attachment(Attachment.builder()
                .filename("cat.jpg")
                .contentType("image/jpeg")
                .content(CONTENT)
                .id("cat")
                .disposition("inline")
                .build())
            .build();

        ResendEmailRequest.Builder builder = composer.compose(email);

        assertNull(builder.getIdempotencyKey());
        ResendEmailRequest request = builder.build();
        assertEquals("Sender <sender@example.com>", request.from());
        assertEquals(List.of("Receiver <receiver@example.com>", "other@example.com"), request.to());
        assertEquals(List.of("cc@example.com"), request.cc());
        assertEquals(List.of("Blind <bcc@example.com>"), request.bcc());
        assertEquals(List.of("support@example.com", "Sales <sales@example.com>"), request.replyTo());
        assertEquals("Hello", request.subject());
        assertEquals("<strong>HTML body</strong>", request.html());
        assertEquals("Text body", request.text());
        String base64 = Base64.getEncoder().encodeToString(CONTENT);
        assertEquals(List.of(
            new ResendAttachment(base64, "note.txt", null, "text/plain", null),
            new ResendAttachment(base64, "cat.jpg", null, "image/jpeg", "cat")
        ), request.attachments());
        assertNull(request.headers());
        assertNull(request.tags());
        assertNull(request.scheduledAt());
        assertNull(request.topicId());
        assertNull(request.template());
    }

    @Test
    void emailWithoutOptionalFields(ResendEmailComposer composer) {
        Email email = Email.builder()
            .from("sender@example.com")
            .to("receiver@example.com")
            .subject("Hello")
            .body("Text body")
            .build();

        ResendEmailRequest request = composer.compose(email).build();

        assertEquals("sender@example.com", request.from());
        assertEquals(List.of("receiver@example.com"), request.to());
        assertEquals("Text body", request.text());
        assertNull(request.html());
        assertNull(request.cc());
        assertNull(request.bcc());
        assertNull(request.replyTo());
        assertNull(request.attachments());
    }

    @Test
    void consumerCustomizesTheRequest(ResendEmailComposer composer) {
        Email email = Email.builder()
            .from("sender@example.com")
            .to("receiver@example.com")
            .subject("Hello")
            .body("Text body")
            .build();

        ResendEmailRequest.Builder builder = composer.compose(email, request -> request
            .tag("category", "welcome")
            .header("X-Entity-Ref-ID", "123")
            .scheduledAt("in 1 min")
            .topicId("topic")
            .addCc("cc@example.com")
            .idempotencyKey("welcome/123"));

        assertNotNull(builder);
        assertEquals("welcome/123", builder.getIdempotencyKey());
        ResendEmailRequest request = builder.build();
        assertEquals(List.of(new ResendTag("category", "welcome")), request.tags());
        assertEquals(Map.of("X-Entity-Ref-ID", "123"), request.headers());
        assertEquals("in 1 min", request.scheduledAt());
        assertEquals("topic", request.topicId());
        assertEquals(List.of("cc@example.com"), request.cc());
        assertEquals(List.of("receiver@example.com"), request.to());
    }
}
