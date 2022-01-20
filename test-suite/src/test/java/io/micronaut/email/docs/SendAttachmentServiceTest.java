package io.micronaut.email.docs;

import io.micronaut.email.Email;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(startApplication = false)
public class SendAttachmentServiceTest {

    @Inject
    SendAttachmentService sendAttachmentService;

    @Inject
    MockEmailSender<?> emailSender;

    @Test
    void transactionalEmailIsCorrectlyBuilt() throws IOException {
        //when:
        sendAttachmentService.sendReport();
        //then:
        assertEquals(1, emailSender.getEmails().size());
        Email email = emailSender.getEmails().get(0);
        assertEquals("sender@example.com", email.getFrom().getEmail());
        assertNull(email.getFrom().getName());
        assertEquals(1, email.getTo().size());
        assertEquals("john@example.com", email.getTo().stream().findFirst().get().getEmail());
        assertNull(email.getTo().stream().findFirst().get().getName());
        assertNull(email.getCc());
        assertNull(email.getBcc());
        assertEquals("Monthly reports", email.getSubject());
        assertEquals("Attached Monthly reports", email.getText());
        assertEquals("<html><body><strong>Attached Monthly reports</strong>.</body></html>", email.getHtml());
        assertNotNull(email.getAttachments());
        assertEquals("reports.xlsx", email.getAttachments().get(0).getFilename());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", email.getAttachments().get(0).getContentType());
        assertNotNull(email.getAttachments().get(0).getContent());
    }
}
