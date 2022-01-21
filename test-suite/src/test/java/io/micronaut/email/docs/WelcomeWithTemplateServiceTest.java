package io.micronaut.email.docs;

import io.micronaut.email.BodyType;
import io.micronaut.email.Email;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class WelcomeWithTemplateServiceTest {

    @Inject
    WelcomeWithTemplateService welcomeService;

    @Inject
    MockEmailSender<?> emailSender;

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

        Email email = emailSender.getEmails().get(0);
        assertEquals("sender@example.com", email.getFrom().getEmail());
        assertNull(email.getFrom().getName());
        assertEquals(1, email.getTo().size());
        assertEquals("john@example.com", email.getTo().stream().findFirst().get().getEmail());
        assertNull(email.getTo().stream().findFirst().get().getName());
        assertNull(email.getCc());
        assertNull(email.getBcc());
        assertEquals("Micronaut test", email.getSubject());
        assertNotNull(email.getBody());
        assertTrue(email.getBody().get(BodyType.TEXT).isPresent());

        String text = email.getBody().get(BodyType.TEXT).get();
        assertTrue(text.contains(message));
        assertTrue(text.contains(copyright));
        assertTrue(text.contains(address));
        assertTrue(email.getBody().get(BodyType.HTML).isPresent());

        String html = email.getBody().get(BodyType.HTML).get();
        assertTrue(html.contains("<h2 class=\"cit\">" + message + "</h2>"));
        assertTrue(html.contains("<div>" + copyright + "</div>"));
        assertTrue(html.contains("<div>" + address + "</div>"));
    }


}
