package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitAttachment;
import io.micronaut.email.mailpit.client.model.MailpitAttachmentChecksums;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitAttachmentSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"PartID":"part-1","FileName":"note.txt","ContentType":"text/plain","ContentID":"content-1","Size":32,"Checksums":{"MD5":"md5","SHA1":"sha1","SHA256":"sha256"}}""",
            jsonMapper.writeValueAsString(new MailpitAttachment(
                "part-1",
                "note.txt",
                "text/plain",
                "content-1",
                32,
                new MailpitAttachmentChecksums("md5", "sha1", "sha256")
            ))
        );
    }
}
