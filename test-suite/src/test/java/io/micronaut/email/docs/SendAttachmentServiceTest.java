package io.micronaut.email.docs;

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
    MockEmailSender emailSender;

    @Test
    void transactionalEmailIsCorrectlyBuilt() throws IOException {
        //when:
        sendAttachmentService.sendReport();
        //then:
        assertEquals(1, emailSender.getEmails().size());
        assertEquals("sender@example.com", emailSender.getEmails().get(0).getFrom().getEmail());
        assertNull(emailSender.getEmails().get(0).getFrom().getName());
        assertEquals(1, emailSender.getEmails().get(0).getTo().size());
        assertEquals("john@example.com", emailSender.getEmails().get(0).getTo().stream().findFirst().get().getEmail());
        assertNull(emailSender.getEmails().get(0).getTo().stream().findFirst().get().getName());
        assertNull(emailSender.getEmails().get(0).getCc());
        assertNull(emailSender.getEmails().get(0).getBcc());
        assertEquals("Monthly reports", emailSender.getEmails().get(0).getSubject());
        assertEquals("Attached Monthly reports", emailSender.getEmails().get(0).getText());
        assertEquals("<html><body><strong>Attached Monthly reports</strong>.</body></html>", emailSender.getEmails().get(0).getHtml());
        assertNotNull(emailSender.getEmails().get(0).getAttachments());
        assertEquals("reports.xlsx", emailSender.getEmails().get(0).getAttachments().get(0).getFilename());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", emailSender.getEmails().get(0).getAttachments().get(0).getContentType());
        assertNotNull(emailSender.getEmails().get(0).getAttachments().get(0).getContent());
    }
}
