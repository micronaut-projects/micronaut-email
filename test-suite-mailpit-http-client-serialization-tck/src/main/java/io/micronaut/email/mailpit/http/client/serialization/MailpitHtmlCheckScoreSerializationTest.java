package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitHtmlCheckScore;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitHtmlCheckScoreSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Found":1,"Supported":60.0,"Partial":20.0,"Unsupported":20.0}""",
            jsonMapper.writeValueAsString(new MailpitHtmlCheckScore(1, 60.0, 20.0, 20.0))
        );
    }
}
