package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitAddress;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitAddressSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Address":"sender@example.com","Name":"Sender"}""",
            jsonMapper.writeValueAsString(new MailpitAddress("sender@example.com", "Sender"))
        );
    }
}
