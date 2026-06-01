package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitSpamAssassinResponse;
import io.micronaut.email.mailpit.client.model.MailpitSpamAssassinRule;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitSpamAssassinResponseSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Error":"spam detected","IsSpam":true,"Rules":[{"Description":"Bayesian spam probability","Name":"BAYES_99","Score":5.5}],"Score":5.5}""",
            jsonMapper.writeValueAsString(new MailpitSpamAssassinResponse(
                "spam detected",
                true,
                List.of(new MailpitSpamAssassinRule("Bayesian spam probability", "BAYES_99", 5.5)),
                5.5
            ))
        );
    }
}
