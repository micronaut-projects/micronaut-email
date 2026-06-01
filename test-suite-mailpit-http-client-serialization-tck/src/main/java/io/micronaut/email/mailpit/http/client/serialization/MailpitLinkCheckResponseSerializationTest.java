package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitLink;
import io.micronaut.email.mailpit.client.model.MailpitLinkCheckResponse;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitLinkCheckResponseSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Errors":0,"Links":[{"URL":"https://example.com","StatusCode":200,"Status":"OK"}]}""",
            jsonMapper.writeValueAsString(new MailpitLinkCheckResponse(
                0,
                List.of(new MailpitLink("https://example.com", 200, "OK"))
            ))
        );
    }
}
