package io.micronaut.email.docs;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest(startApplication = false)
public class WelcomeWithTemplateServiceTest {

    @Inject
    WelcomeWithTemplateService welcomeService;

    @Inject
    MockEmailSender emailSender;

    @Test
    void transactionalEmailIsCorrectlyBuilt() {
        //given
        String message = "Hello dear Micronaut user";
        String copyright =  "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED";
        String address = "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141";
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
        assertTrue(emailSender.getEmails().get(0).getText().contains(message));
        assertTrue(emailSender.getEmails().get(0).getText().contains(copyright));
        assertTrue(emailSender.getEmails().get(0).getText().contains(address));
        assertTrue(emailSender.getEmails().get(0).getHtml().contains("<h2 class=\"cit\">" + message + "</h2>"));
        assertTrue(emailSender.getEmails().get(0).getHtml().contains("<div>" + copyright + "</div>"));
        assertTrue(emailSender.getEmails().get(0).getHtml().contains("<div>" + address + "</div>"));
    }
}
