package io.micronaut.email.mailtrap;

import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import io.micronaut.email.Attachment;
import io.micronaut.email.Email;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class MailtrapEmailComposerTest {
    private static final String SENDER_EMAIL = "sender@domain.com";
    private static final String RECIPIENT_EMAIL = "recipient@domain.com";

    @Test
    void testMailtrapEmailComposer(MailtrapEmailComposer composer) {
        String subject = "Hello from Mailtrap Sending!";
        String text = "Welcome to Mailtrap Sending!";
        Email email = Email.builder()
            .from(SENDER_EMAIL)
            .to(RECIPIENT_EMAIL)
            .subject(subject)
            .body(text)
            .attachment(Attachment.builder()
                .filename("welcome.png")
                .id("welcome.png")
                .disposition("inline")
                .contentType("image/jpg")
                .content(new File("src/test/resources/cat.jpg"))
                .build())
            .build();
        MailtrapMail mail = composer.compose(email).build();
        final MailtrapMail expected = MailtrapMail.builder()
            .from(new Address(SENDER_EMAIL))
            .to(List.of(new Address(RECIPIENT_EMAIL)))
            .subject(subject)
            .text(text)
            .build();
        assertEquals(expected.getTo().get(0).getEmail(), mail.getTo().get(0).getEmail());
        assertEquals(expected.getFrom().getEmail(), mail.getFrom().getEmail());
        assertEquals(expected.getCc(), mail.getCc());
        assertEquals(expected.getBcc(), mail.getBcc());
        assertEquals(expected.getSubject(), mail.getSubject());
        assertEquals(expected.getText(), mail.getText());
        assertEquals(1, mail.getAttachments().size());
    }
}
