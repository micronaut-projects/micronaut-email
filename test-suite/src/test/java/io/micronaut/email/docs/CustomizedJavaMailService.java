package io.micronaut.email.docs;

import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import io.micronaut.email.MultipartBody;
import jakarta.inject.Singleton;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An example of customization for JavaMail messages
 */
@Singleton
public class CustomizedJavaMailService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomizedJavaMailService.class);

    private final EmailSender<Message, ?> emailSender;

    public CustomizedJavaMailService(EmailSender<Message, ?> emailSender) {
        this.emailSender = emailSender;
    }

    public void sendCustomizedEmail() {
        Email.Builder email = Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .body(
                        new MultipartBody(
                                "<html><body><strong>Hello</strong> dear Micronaut user.</body></html>",
                                "Hello dear Micronaut user"
                        )
                );

        // Customize the message with a header prior to sending
        emailSender.send(email, message -> {
            try {
                message.addHeader("List-Unsubscribe", "<mailto:list@host.com?subject=unsubscribe>");
            } catch (MessagingException e) {
                LOG.error("Failed to add header", e);
            }
        });
    }
}
