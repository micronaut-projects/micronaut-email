package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitHtmlCheckTotal;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitHtmlCheckTotalSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Tests":10,"Nodes":20,"Supported":70.0,"Partial":20.0,"Unsupported":10.0}""",
            jsonMapper.writeValueAsString(new MailpitHtmlCheckTotal(10, 20, 70.0, 20.0, 10.0))
        );
    }
}
