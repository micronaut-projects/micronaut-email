package io.micronaut.email.mailtrap;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.email.Attachment;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfEnvironmentVariable(
    named = "MAILTRAP_TOKEN",
    matches = ".+"
)
@Property(name = "mailtrap.sandbox", value = StringUtils.TRUE)
@Property(name = "mailtrap.inbox-id", value = "4215558")
@MicronautTest(startApplication = false)
class MailtrapFunctionalTest {
    private static final String SENDER_EMAIL = "micronautemailtest@gmail.com";
    private static final String RECIPIENT_EMAIL = "marketing@micronaut.io";

    @Test
    void testSendMail(EmailSender<?, ?> emailSender) {
        File f = new File("src/test/resources/cat.jpg");
        File dog = new File("src/test/resources/dog.jpg");
        assertTrue(f.exists());
        assertTrue(dog.exists());
        Attachment attachment = Attachment.builder()
            .filename("dog.jpg")
            .content(dog)
            .contentType("image/jpeg")
            .build();
        Attachment inlineAttachment = Attachment.builder()
            .filename("cat.jpg")
            .content(f)
            .contentType("image/jpeg")
            .id("cat")
            .disposition("inline")
            .build();
        String html = """
        <html>
        <body>
            <h1>Check out this cute cat!</h1>
            <p>Here's an inline image embedded in the email:</p>
            <img src="cid:cat" alt="Cute Cat" style="max-width: 600px; height: auto;">
            <p>Isn't it adorable?</p>
        </body>
        </html>
    """;

        String text = """
        Check out this cute cat!

        Here's an inline image embedded in the email.
        (Note: Plain text clients won't display the image)

        Isn't it adorable?
    """;
        Email.Builder builder = Email.builder()
            .subject("inline attachments")
            .from(SENDER_EMAIL)
            .to(new Contact(RECIPIENT_EMAIL))
            .attachment(inlineAttachment)
            .attachment(attachment)
            .body(html, text);
        assertDoesNotThrow(() -> emailSender.send(builder));
    }
}
