package io.micronaut.email.mailpit.http.client.serialization;

import io.micronaut.email.mailpit.client.model.MailpitHtmlCheckResult;
import io.micronaut.email.mailpit.client.model.MailpitHtmlCheckScore;
import io.micronaut.email.mailpit.client.model.MailpitHtmlCheckWarning;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitHtmlCheckWarningSerializationTest {

    @Test
    void serializesToExpectedJson(JsonMapper jsonMapper) throws Exception {
        assertEquals(
            """
                {"Slug":"css-position","Title":"CSS position","Description":"Position is not supported","URL":"https://example.com/css-position","Category":"CSS","Tags":["css"],"Keywords":"position","Results":[{"Name":"Outlook","Platform":"Windows","Family":"Outlook","Version":"2021","Support":"unsupported","NoteNumber":"1"}],"NotesByNumber":{"1":"Avoid position"},"Score":{"Found":1,"Supported":60.0,"Partial":20.0,"Unsupported":20.0}}""",
            jsonMapper.writeValueAsString(new MailpitHtmlCheckWarning(
                "css-position",
                "CSS position",
                "Position is not supported",
                "https://example.com/css-position",
                "CSS",
                List.of("css"),
                "position",
                List.of(new MailpitHtmlCheckResult("Outlook", "Windows", "Outlook", "2021", "unsupported", "1")),
                Map.of("1", "Avoid position"),
                new MailpitHtmlCheckScore(1, 60.0, 20.0, 20.0)
            ))
        );
    }
}
