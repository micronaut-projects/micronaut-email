package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitAttachmentChecksums;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitAttachmentChecksumsSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"MD5":"md5","SHA1":"sha1","SHA256":"sha256"}""",
            jsonMapper.writeValueAsString(new MailpitAttachmentChecksums("md5", "sha1", "sha256"))
        );
    }
}
