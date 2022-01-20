package io.micronaut.email.docs;

import io.micronaut.email.Email;
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
    MockEmailSender<?> emailSender;

    @Test
    void transactionalHtmlEmailIsCorrectlyBuilt() {
        //when:
        welcomeService.sendWelcomeEmailHtml();
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
        assertEquals("Micronaut test", email.getSubject());
        assertEquals("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>", email.getBody().get());
    }

    @Test
    void transactionalTextEmailIsCorrectlyBuilt() {
        //when:
        welcomeService.sendWelcomeEmailText();
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
        assertEquals("Hello dear Micronaut user", email.getBody().get());
    }
}
