package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitAddress;
import io.micronaut.email.mailpit.client.model.MailpitMessageSummary;
import io.micronaut.email.mailpit.client.model.MailpitMessagesSummary;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitMessagesSummarySerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"total":5,"unread":2,"count":1,"messages_count":5,"messages_unread":2,"start":0,"tags":["Important"],"messages":[{"ID":"message-1","MessageID":"<message-1@example.com>","Read":false,"From":{"Address":"sender@example.com","Name":"Sender"},"To":[{"Address":"sender@example.com","Name":"Sender"}],"Cc":[{"Address":"sender@example.com","Name":"Sender"}],"Bcc":[{"Address":"sender@example.com","Name":"Sender"}],"ReplyTo":[{"Address":"sender@example.com","Name":"Sender"}],"Subject":"Subject","Created":"2026-05-29T12:00:00Z","Username":"user","Tags":["Important"],"Size":512,"Attachments":1,"Snippet":"Plain body"}]}""",
            jsonMapper.writeValueAsString(new MailpitMessagesSummary(
                5,
                2,
                1,
                5,
                2,
                0,
                List.of("Important"),
                List.of(messageSummary())
            ))
        );
    }

    private static MailpitAddress address() {
        return new MailpitAddress("sender@example.com", "Sender");
    }

    private static MailpitMessageSummary messageSummary() {
        return new MailpitMessageSummary(
            "message-1",
            "<message-1@example.com>",
            false,
            address(),
            List.of(address()),
            List.of(address()),
            List.of(address()),
            List.of(address()),
            "Subject",
            "2026-05-29T12:00:00Z",
            "user",
            List.of("Important"),
            512,
            1,
            "Plain body"
        );
    }
}
