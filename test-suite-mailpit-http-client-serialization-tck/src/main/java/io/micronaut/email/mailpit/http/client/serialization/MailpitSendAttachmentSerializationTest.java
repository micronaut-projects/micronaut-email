package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitSendAttachment;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitSendAttachmentSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Content":"YXR0YWNobWVudA==","Filename":"note.txt","ContentType":"text/plain","ContentID":"content-1"}""",
            jsonMapper.writeValueAsString(new MailpitSendAttachment(
                "YXR0YWNobWVudA==",
                "note.txt",
                "text/plain",
                "content-1"
            ))
        );
    }
}
