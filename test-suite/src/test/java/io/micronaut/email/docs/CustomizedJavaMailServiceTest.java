package io.micronaut.email.docs;

import com.sun.mail.imap.DefaultFolder;
import io.micronaut.email.BodyType;
import io.micronaut.email.Email;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class CustomizedJavaMailServiceTest {

    @Inject
    CustomizedJavaMailService customizedJavaMailService;

    @Inject
    MockEmailSender<Message> emailSender;

    @Test
    void transactionalHtmlEmailIsCorrectlyBuilt() {
        //when:
        customizedJavaMailService.sendCustomizedEmail();

        //then:
        assertEquals(1, emailSender.getEmails().size());
        Email email = emailSender.getEmails().get(0);
        Consumer<Message> consumer = emailSender.getRequests().get(0);
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
        assertEquals("Hello dear Micronaut user", email.getBody().get(BodyType.TEXT).get());
        assertTrue(email.getBody().get(BodyType.HTML).isPresent());
        assertEquals("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>",  email.getBody().get(BodyType.HTML).get());

        //when:
        MessageHeaderCapture message = new MessageHeaderCapture();
        consumer.accept(message);

        //then:
        assertEquals("List-Unsubscribe", message.name);
        assertEquals("<mailto:list@host.com?subject=unsubscribe>", message.value);
    }

    static class MessageHeaderCapture extends MimeMessage {

        String name;
        String value;

        public MessageHeaderCapture() {
            super((Session) null);
        }

        @Override
        public void addHeader(String name, String value) throws MessagingException {
            this.name = name;
            this.value = value;
        }
    }
}
