package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitAppInformation;
import io.micronaut.email.mailpit.client.model.MailpitRuntimeStats;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitAppInformationSerializationTest {
    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Version":"1.27.8","LatestVersion":"1.28.0","Database":"/tmp/mailpit.db","DatabaseSize":1024,"Messages":5,"Unread":2,"Tags":{"Important":3},"RuntimeStats":{"Uptime":10,"Memory":2048,"MessagesDeleted":1,"SMTPAccepted":2,"SMTPAcceptedSize":512,"SMTPRejected":3,"SMTPIgnored":4}}""",
            jsonMapper.writeValueAsString(new MailpitAppInformation(
                "1.27.8",
                "1.28.0",
                "/tmp/mailpit.db",
                1024,
                5,
                2,
                Map.of("Important", 3L),
                new MailpitRuntimeStats(10, 2048, 1, 2, 512, 3, 4)
            ))
        );
    }
}
