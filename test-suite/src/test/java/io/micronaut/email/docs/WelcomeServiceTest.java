package io.micronaut.email.docs;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class WelcomeServiceTest {

    @Inject
    WelcomeService welcomeService;

    @Inject
    MockEmailSender emailSender;

    @Test
    void transactionalEmailIsCorrectlyBuilt() {
        //when:
        welcomeService.sendWelcomeEmail();
        //then:
        assertEquals(1, emailSender.getEmails().size());
        assertEquals("sender@example.com", emailSender.getEmails().get(0).getFrom().getEmail());
        assertNull(emailSender.getEmails().get(0).getFrom().getName());
        assertEquals(1, emailSender.getEmails().get(0).getTo().size());
        assertEquals("john@example.com", emailSender.getEmails().get(0).getTo().get(0).getEmail());
        assertNull(emailSender.getEmails().get(0).getTo().get(0).getName());
        assertNull(emailSender.getEmails().get(0).getCc());
        assertNull(emailSender.getEmails().get(0).getBcc());
        assertEquals("Micronaut test", emailSender.getEmails().get(0).getSubject());
        assertEquals("Hello dear Micronaut user", emailSender.getEmails().get(0).getText());
        assertEquals("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>", emailSender.getEmails().get(0).getHtml());
    }
}
