package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitMessageRelayConfiguration;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitMessageRelayConfigurationSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Enabled":true,"SMTPServer":"smtp.example.com:587","ReturnPath":"bounce@example.com","AllowedRecipients":"*@example.com","BlockedRecipients":"blocked@example.com","OverrideFrom":"sender@example.com","PreserveMessageIDs":true,"RecipientAllowlist":"*@example.com"}""",
            jsonMapper.writeValueAsString(new MailpitMessageRelayConfiguration(
                true,
                "smtp.example.com:587",
                "bounce@example.com",
                "*@example.com",
                "blocked@example.com",
                "sender@example.com",
                true,
                "*@example.com"
            ))
        );
    }
}
