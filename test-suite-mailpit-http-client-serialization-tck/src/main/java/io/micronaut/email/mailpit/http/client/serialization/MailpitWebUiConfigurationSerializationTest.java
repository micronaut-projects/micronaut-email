package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitMessageRelayConfiguration;
import io.micronaut.email.mailpit.client.model.MailpitWebUiConfiguration;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitWebUiConfigurationSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Label":"Local Mailpit","MessageRelay":{"Enabled":true,"SMTPServer":"smtp.example.com:587","ReturnPath":"bounce@example.com","AllowedRecipients":"*@example.com","BlockedRecipients":"blocked@example.com","OverrideFrom":"sender@example.com","PreserveMessageIDs":true,"RecipientAllowlist":"*@example.com"},"SpamAssassin":true,"ChaosEnabled":false,"DuplicatesIgnored":true,"HideDeleteAllButton":false}""",
            jsonMapper.writeValueAsString(new MailpitWebUiConfiguration(
                "Local Mailpit",
                new MailpitMessageRelayConfiguration(
                    true,
                    "smtp.example.com:587",
                    "bounce@example.com",
                    "*@example.com",
                    "blocked@example.com",
                    "sender@example.com",
                    true,
                    "*@example.com"
                ),
                true,
                false,
                true,
                false
            ))
        );
    }
}
