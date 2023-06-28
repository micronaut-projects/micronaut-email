package example.micronaut;

import io.micronaut.email.Attachment;
import io.micronaut.email.Email;
import io.micronaut.email.ses.SesEmailComposer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(startApplication = false)
public class SesEmailComposerServiceTest {

    @Inject
    SesEmailComposer composer;

    @Test
    void testRawEmailRequest() throws IOException {
        SendRawEmailRequest request = (SendRawEmailRequest) composer.compose(
            Email.builder()
                .to("tim@bloidonia.com")
                .from("tim@bloidonia.com")
                .subject("test")
                .body("It's " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .attachment(
                    Attachment.builder()
                        .filename("attachment.pdf")
                        .contentType("application/pdf")
                        .content(getClass().getResourceAsStream("/hi.pdf").readAllBytes())
                        .build()
                )
                .build()
        );

        assertNotNull(request.getValueForField("RawMessage", RawMessage.class));
    }

    @Test
    void testEmailRequest() {
        SendEmailRequest request = (SendEmailRequest) composer.compose(
            Email.builder()
                .to("to_dest@test.com")
                .cc("cc_dest@test.com")
                .bcc("bcc_dest@test.com")
                .from("source@test.com")
                .subject("test")
                .body("It's " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build()
        );

        assertEquals("source@test.com", request.source());
        assertEquals(List.of("to_dest@test.com"), request.destination().toAddresses());
        assertEquals(List.of("cc_dest@test.com"), request.destination().ccAddresses());
        assertEquals(List.of("bcc_dest@test.com"), request.destination().bccAddresses());
        assertEquals("test", request.message().subject().data());
    }
}
