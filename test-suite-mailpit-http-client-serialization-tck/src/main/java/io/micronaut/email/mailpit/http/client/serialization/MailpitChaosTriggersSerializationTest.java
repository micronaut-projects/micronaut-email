package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitChaosTrigger;
import io.micronaut.email.mailpit.client.model.MailpitChaosTriggers;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitChaosTriggersSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Sender":{"ErrorCode":451,"Probability":25},"Recipient":{"ErrorCode":452,"Probability":50},"Authentication":{"ErrorCode":535,"Probability":75}}""",
            jsonMapper.writeValueAsString(new MailpitChaosTriggers(
                new MailpitChaosTrigger(451, 25),
                new MailpitChaosTrigger(452, 50),
                new MailpitChaosTrigger(535, 75)
            ))
        );
    }
}
