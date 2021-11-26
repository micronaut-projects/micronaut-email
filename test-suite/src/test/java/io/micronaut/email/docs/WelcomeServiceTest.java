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
    MockEmailCourier emailCourier;

    @Test
    void transactionalEmailIsCorrectlyBuilt() {
        //when:
        welcomeService.sendWelcomeEmail();
        //then:
        assertEquals(1, emailCourier.getEmails().size());
        assertEquals("sender@example.com", emailCourier.getEmails().get(0).getSender().getFrom().getEmail());
        assertNull(emailCourier.getEmails().get(0).getSender().getFrom().getName());
        assertEquals(1, emailCourier.getEmails().get(0).getTo().size());
        assertEquals("john@example.com", emailCourier.getEmails().get(0).getTo().get(0).getEmail());
        assertNull(emailCourier.getEmails().get(0).getTo().get(0).getName());
        assertNull(emailCourier.getEmails().get(0).getCc());
        assertNull(emailCourier.getEmails().get(0).getBcc());
        assertEquals("Micronaut test", emailCourier.getEmails().get(0).getSubject());
        assertEquals("Hello dear Micronaut user", emailCourier.getEmails().get(0).getText());
        assertEquals("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>", emailCourier.getEmails().get(0).getHtml());
    }
}
