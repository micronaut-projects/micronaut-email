package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitHtmlCheckResult;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitHtmlCheckResultSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Name":"Outlook","Platform":"Windows","Family":"Outlook","Version":"2021","Support":"unsupported","NoteNumber":"1"}""",
            jsonMapper.writeValueAsString(new MailpitHtmlCheckResult("Outlook", "Windows", "Outlook", "2021", "unsupported", "1"))
        );
    }
}
