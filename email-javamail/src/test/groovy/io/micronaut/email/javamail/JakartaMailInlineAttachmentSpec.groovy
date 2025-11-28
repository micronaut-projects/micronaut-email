package io.micronaut.email.javamail

import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.NonNull
import io.micronaut.email.*
import io.micronaut.email.javamail.sender.MailPropertiesProvider
import io.micronaut.email.javamail.sender.SessionProvider
import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.mail.Session
import spock.lang.Requires
import spock.lang.Specification

@Requires({ env["MAILTRAP_PASSWORD"] })
class JakartaMailInlineAttachmentSpec extends Specification {

    @Inject
    EmailSender emailSender

    void "inline attachments"() {
        given:
        String host = "live.smtp.mailtrap.io"
        ApplicationContext applicationContext = ApplicationContext.run([
                "spec.name": "JakartaMailInlineAttachmentSpec",
                'javamail.properties': ['mail.smtp.auth': "true",
                                        'mail.smtp.starttls.enable': "true",
                                        'mail.smtp.host': host,
                                        "mail.smtp.port": "587"]])
        EmailSender emailSender = applicationContext.getBean(EmailSender)
        File f = new File("src/test/resources/cat.jpg")
        File dog = new File("src/test/resources/dog.jpg")

        expect:
        f.exists()

        when:
        Attachment attachment = Attachment.builder()
                .filename("dog.jpg")
                .content(dog)
                .contentType("image/jpeg")
                .build()
        Attachment inlineAttachment = Attachment.builder()
                .filename("cat.jpg")
                .content(f)
                .contentType("image/jpeg")
                .id("cat")
                .build()
        String html = """
        <html>
        <body>
            <h1>Check out this cute cat!</h1>
            <p>Here's an inline image embedded in the email:</p>
            <img src="cid:cat" alt="Cute Cat" style="max-width: 600px; height: auto;">
            <p>Isn't it adorable?</p>
        </body>
        </html>
    """.stripIndent()

        String text = """
        Check out this cute cat!

        Here's an inline image embedded in the email.
        (Note: Plain text clients won't display the image)

        Isn't it adorable?
    """.stripIndent()
        Email.Builder builder = Email.builder()
                .subject("inline attachments")
                .from("hello@demomailtrap.co")
                .to(new Contact("micronautemailtest@gmail.com"))
                .attachment(inlineAttachment)
                .attachment(attachment)
                .body(html, text)
        emailSender.send(builder)

        then:
        noExceptionThrown()

        cleanup:
        applicationContext.close()
    }

    @io.micronaut.context.annotation.Requires(property = "spec.name", value = "JakartaMailInlineAttachmentSpec")
    @Singleton
    static class MailtrapSessionProvider implements SessionProvider {
        private final Properties properties;

        MailtrapSessionProvider(MailPropertiesProvider mailPropertiesProvider) {
            this.properties = mailPropertiesProvider.mailProperties()
        }

        @Override
        @NonNull
        Session session() {
            return Session.getInstance(properties, new jakarta.mail.Authenticator() {
                @Override
                protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                    new jakarta.mail.PasswordAuthentication("api", System.getenv("MAILTRAP_PASSWORD"))
                }
            })
        }
    }
}
