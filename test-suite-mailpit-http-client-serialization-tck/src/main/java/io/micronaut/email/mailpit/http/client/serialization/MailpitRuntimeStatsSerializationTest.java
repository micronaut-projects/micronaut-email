package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitRuntimeStats;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitRuntimeStatsSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Uptime":10,"Memory":2048,"MessagesDeleted":1,"SMTPAccepted":2,"SMTPAcceptedSize":512,"SMTPRejected":3,"SMTPIgnored":4}""",
            jsonMapper.writeValueAsString(new MailpitRuntimeStats(10, 2048, 1, 2, 512, 3, 4))
        );
    }
}
