package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitListUnsubscribe;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitListUnsubscribeSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Header":"<mailto:unsubscribe@example.com>","Links":["mailto:unsubscribe@example.com"],"Errors":"none","HeaderPost":"List-Unsubscribe=One-Click"}""",
            jsonMapper.writeValueAsString(new MailpitListUnsubscribe(
                "<mailto:unsubscribe@example.com>",
                List.of("mailto:unsubscribe@example.com"),
                "none",
                "List-Unsubscribe=One-Click"
            ))
        );
    }
}
