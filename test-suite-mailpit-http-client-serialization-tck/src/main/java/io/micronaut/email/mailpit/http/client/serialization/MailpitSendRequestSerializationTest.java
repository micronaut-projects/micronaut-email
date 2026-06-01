package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitSendAddress;
import io.micronaut.email.mailpit.client.model.MailpitSendAttachment;
import io.micronaut.email.mailpit.client.model.MailpitSendRequest;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitSendRequestSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"From":{"Email":"receiver@example.com","Name":"Receiver"},"To":[{"Email":"receiver@example.com","Name":"Receiver"}],"Cc":[{"Email":"receiver@example.com","Name":"Receiver"}],"Bcc":["bcc@example.com"],"ReplyTo":[{"Email":"receiver@example.com","Name":"Receiver"}],"Subject":"Subject","Text":"Plain body","HTML":"<p>HTML body</p>","Attachments":[{"Content":"YXR0YWNobWVudA==","Filename":"note.txt","ContentType":"text/plain","ContentID":"content-1"}],"Tags":["Important"],"Headers":{"X-Test":"mailpit"}}""",
            jsonMapper.writeValueAsString(new MailpitSendRequest(
                sendAddress(),
                List.of(sendAddress()),
                List.of(sendAddress()),
                List.of("bcc@example.com"),
                List.of(sendAddress()),
                "Subject",
                "Plain body",
                "<p>HTML body</p>",
                List.of(new MailpitSendAttachment("YXR0YWNobWVudA==", "note.txt", "text/plain", "content-1")),
                List.of("Important"),
                Map.of("X-Test", "mailpit")
            ))
        );
    }

    private static MailpitSendAddress sendAddress() {
        return new MailpitSendAddress("receiver@example.com", "Receiver");
    }
}
