package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitAddress;
import io.micronaut.email.mailpit.client.model.MailpitAttachment;
import io.micronaut.email.mailpit.client.model.MailpitAttachmentChecksums;
import io.micronaut.email.mailpit.client.model.MailpitListUnsubscribe;
import io.micronaut.email.mailpit.client.model.MailpitMessage;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitMessageSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"ID":"message-1","MessageID":"<message-1@example.com>","From":{"Address":"sender@example.com","Name":"Sender"},"To":[{"Address":"sender@example.com","Name":"Sender"}],"Cc":[{"Address":"sender@example.com","Name":"Sender"}],"Bcc":[{"Address":"sender@example.com","Name":"Sender"}],"ReplyTo":[{"Address":"sender@example.com","Name":"Sender"}],"ReturnPath":"bounce@example.com","Subject":"Subject","ListUnsubscribe":{"Header":"<mailto:unsubscribe@example.com>","Links":["mailto:unsubscribe@example.com"],"Errors":"none","HeaderPost":"List-Unsubscribe=One-Click"},"Date":"2026-05-29T12:00:00Z","Tags":["Important"],"Username":"user","Text":"Plain body","HTML":"<p>HTML body</p>","Size":512,"Inline":[{"PartID":"part-1","FileName":"note.txt","ContentType":"text/plain","ContentID":"content-1","Size":32,"Checksums":{"MD5":"md5","SHA1":"sha1","SHA256":"sha256"}}],"Attachments":[{"PartID":"part-1","FileName":"note.txt","ContentType":"text/plain","ContentID":"content-1","Size":32,"Checksums":{"MD5":"md5","SHA1":"sha1","SHA256":"sha256"}}]}""",
            jsonMapper.writeValueAsString(new MailpitMessage(
                "message-1",
                "<message-1@example.com>",
                address(),
                List.of(address()),
                List.of(address()),
                List.of(address()),
                List.of(address()),
                "bounce@example.com",
                "Subject",
                listUnsubscribe(),
                "2026-05-29T12:00:00Z",
                List.of("Important"),
                "user",
                "Plain body",
                "<p>HTML body</p>",
                512,
                List.of(attachment()),
                List.of(attachment())
            ))
        );
    }

    private static MailpitAddress address() {
        return new MailpitAddress("sender@example.com", "Sender");
    }

    private static MailpitAttachment attachment() {
        return new MailpitAttachment(
            "part-1",
            "note.txt",
            "text/plain",
            "content-1",
            32,
            new MailpitAttachmentChecksums("md5", "sha1", "sha256")
        );
    }

    private static MailpitListUnsubscribe listUnsubscribe() {
        return new MailpitListUnsubscribe(
            "<mailto:unsubscribe@example.com>",
            List.of("mailto:unsubscribe@example.com"),
            "none",
            "List-Unsubscribe=One-Click"
        );
    }
}
