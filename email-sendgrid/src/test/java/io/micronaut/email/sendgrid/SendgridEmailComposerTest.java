package io.micronaut.email.sendgrid;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "sendgrid.api-key", value = "xxx")
@MicronautTest(startApplication = false)
class SendgridEmailComposerTest {
    @Test
    void createMail(SendgridEmailComposer composer) {
        Email email = email();
        Mail mail = composer.createMail(email);
        assertEquals(2, mail.getContent().size());
    }

    @Test
    void contentOfEmail() {
        Email email = email();
        List<Content> contents = assertDoesNotThrow(() -> SendgridEmailComposer.contentOfEmail(email));
        assertEquals(2, contents.size());
        assertEquals("text/plain", contents.get(0).getType(), "text/plain should be before html");
        assertEquals("text/html", contents.get(1).getType());
    }

    private static Email email() {
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
        return Email.builder()
            .subject("Test")
            .from("micronautemailtest@gmail.com")
            .to(new Contact("marketing@micronaut.io"))
            .body(html, text).build();
    }
}
