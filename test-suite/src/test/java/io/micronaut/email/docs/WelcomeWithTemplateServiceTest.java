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
    MockEmailCourier emailCourier;

    @Test
    void transactionalEmailIsCorrectlyBuilt() {
        //given
        String message = "Hello dear Micronaut user";
        String copyright =  "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED";
        String address = "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141";
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
        assertTrue(emailCourier.getEmails().get(0).getText().contains(message));
        assertTrue(emailCourier.getEmails().get(0).getText().contains(copyright));
        assertTrue(emailCourier.getEmails().get(0).getText().contains(address));
        assertTrue(emailCourier.getEmails().get(0).getHtml().contains("<h2 class=\"cit\">" + message + "</h2>"));
        assertTrue(emailCourier.getEmails().get(0).getHtml().contains("<div>" + copyright + "</div>"));
        assertTrue(emailCourier.getEmails().get(0).getHtml().contains("<div>" + address + "</div>"));
    }
}
