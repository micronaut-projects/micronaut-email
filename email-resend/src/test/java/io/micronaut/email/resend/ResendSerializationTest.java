package io.micronaut.email.resend;

import io.micronaut.core.type.Argument;
import io.micronaut.email.resend.model.ResendAttachment;
import io.micronaut.email.resend.model.ResendEmailRequest;
import io.micronaut.email.resend.model.ResendEmailResponse;
import io.micronaut.email.resend.model.ResendError;
import io.micronaut.email.resend.model.ResendTemplate;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest(startApplication = false)
class ResendSerializationTest {

    @Test
    void minimalRequestOmitsEmptyFields(JsonMapper jsonMapper) throws IOException {
        ResendEmailRequest request = ResendEmailRequest.builder()
            .from("sender@example.com")
            .addTo("receiver@example.com")
            .subject("Hello")
            .text("Text body")
            .idempotencyKey("not-serialized")
            .build();

        assertEquals(
            "{\"from\":\"sender@example.com\",\"to\":[\"receiver@example.com\"],\"subject\":\"Hello\",\"text\":\"Text body\"}",
            jsonMapper.writeValueAsString(request)
        );
    }

    @Test
    void requestUsesResendPropertyNames(JsonMapper jsonMapper) throws IOException {
        ResendEmailRequest request = ResendEmailRequest.builder()
            .from("Sender <sender@example.com>")
            .to(List.of("receiver@example.com"))
            .subject("Hello")
            .cc(List.of("cc@example.com"))
            .bcc(List.of("bcc@example.com"))
            .replyTo(List.of("support@example.com"))
            .html("<strong>HTML body</strong>")
            .text("Text body")
            .header("X-Entity-Ref-ID", "123")
            .attachment(new ResendAttachment("Y29udGVudA==", "note.txt", null, "text/plain", "note"))
            .attachment(new ResendAttachment(null, "remote.pdf", "https://example.com/remote.pdf", null, null))
            .tag("category", "welcome")
            .scheduledAt(Instant.parse("2026-10-01T10:15:30Z"))
            .topicId("topic")
            .build();

        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("from", "Sender <sender@example.com>");
        expected.put("to", List.of("receiver@example.com"));
        expected.put("subject", "Hello");
        expected.put("cc", List.of("cc@example.com"));
        expected.put("bcc", List.of("bcc@example.com"));
        expected.put("reply_to", List.of("support@example.com"));
        expected.put("html", "<strong>HTML body</strong>");
        expected.put("text", "Text body");
        expected.put("headers", Map.of("X-Entity-Ref-ID", "123"));
        expected.put("attachments", List.of(
            Map.of("content", "Y29udGVudA==", "filename", "note.txt", "content_type", "text/plain", "content_id", "note"),
            Map.of("filename", "remote.pdf", "path", "https://example.com/remote.pdf")
        ));
        expected.put("tags", List.of(Map.of("name", "category", "value", "welcome")));
        expected.put("scheduled_at", "2026-10-01T10:15:30Z");
        expected.put("topic_id", "topic");

        assertEquals(expected, toMap(jsonMapper, request));
    }

    @Test
    void templateIsSerialized(JsonMapper jsonMapper) throws IOException {
        Map<String, Object> variables = new LinkedHashMap<>();
        variables.put("name", "Sergio");
        variables.put("seats", 3);
        ResendEmailRequest request = ResendEmailRequest.builder()
            .from("sender@example.com")
            .addTo("receiver@example.com")
            .template(new ResendTemplate("welcome", variables))
            .build();

        assertEquals(Map.of(
            "from", "sender@example.com",
            "to", List.of("receiver@example.com"),
            "template", Map.of("id", "welcome", "variables", Map.of("name", "Sergio", "seats", 3))
        ), toMap(jsonMapper, request));
    }

    @Test
    void responseIsDeserialized(JsonMapper jsonMapper) throws IOException {
        ResendEmailResponse response = jsonMapper.readValue(
            "{\"id\":\"49a3999c-0ce1-4ea6-ab68-afcd6dc2e794\",\"unknown\":true}",
            ResendEmailResponse.class
        );

        assertEquals("49a3999c-0ce1-4ea6-ab68-afcd6dc2e794", response.id());
    }

    @Test
    void errorIsDeserialized(JsonMapper jsonMapper) throws IOException {
        ResendError error = jsonMapper.readValue(
            "{\"statusCode\":422,\"name\":\"validation_error\",\"message\":\"Invalid `from` field.\"}",
            ResendError.class
        );

        assertEquals(422, error.statusCode());
        assertEquals("validation_error", error.name());
        assertEquals("Invalid `from` field.", error.message());

        error = jsonMapper.readValue("{\"message\":\"API key is invalid\"}", ResendError.class);

        assertNull(error.statusCode());
        assertNull(error.name());
        assertEquals("API key is invalid", error.message());
    }

    private static Map<String, Object> toMap(JsonMapper jsonMapper, Object value) throws IOException {
        return jsonMapper.readValue(jsonMapper.writeValueAsString(value), Argument.mapOf(String.class, Object.class));
    }
}
