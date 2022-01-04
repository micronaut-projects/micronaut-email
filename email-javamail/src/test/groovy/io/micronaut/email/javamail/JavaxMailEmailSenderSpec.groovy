package io.micronaut.email.javamail

import io.micronaut.context.annotation.Property
import io.micronaut.core.annotation.NonNull
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.test.MailTestUtils
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import spock.lang.Requires
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

@Property(name = "spec.name", value = "JavaxMailEmailSenderSpec")
@MicronautTest(startApplication = false)
class JavaxMailEmailSenderSpec extends Specification {

    @Inject
    EmailSender emailSender

    @Requires({env["GMAIL_USERNAME"] && env["GMAIL_PASSWORD"]})
    void "Functional test of SES integration"() {
        given:
        String subject = "[Javax Mail] Test"
        String gmail = System.getenv("GMAIL_USERNAME")

        when:
        emailSender.send(Email.builder()
                .from(gmail)
                .to(gmail)
                .subject(subject)
                .text("Hello world")
                .build())
        then:
        new PollingConditions(timeout: 30).eventually {
            1 == MailTestUtils.countAndDeleteInboxEmailsBySubject(gmail, System.getenv("GMAIL_PASSWORD"), subject)
        }
    }

    @io.micronaut.context.annotation.Requires(property = "spec.name", value = "JavaxMailEmailSenderSpec")
    @Singleton
    static class GmailMailPropertiesProvider implements MailPropertiesProvider {
        @Override
        @NonNull
        Properties mailProperties() {
            Properties prop = new Properties()
            prop.put("mail.smtp.host", "smtp.gmail.com")
            prop.put("mail.smtp.socketFactory.port","465")
            prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory")
            prop.put("mail.smtp.auth","true")
            prop.put("mail.smtp.port","465")
            prop
        }
    }

    @io.micronaut.context.annotation.Requires(property = "spec.name", value = "JavaxMailEmailSenderSpec")
    @io.micronaut.context.annotation.Requires(condition = GmailCondition.class)
    @Singleton
    static class GmailSessionProvider implements SessionProvider {
        private final Properties properties;

        GmailSessionProvider(MailPropertiesProvider mailPropertiesProvider) {
            this.properties = mailPropertiesProvider.mailProperties()
        }

        @Override
        @NonNull
        Session session() {
            return Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    new PasswordAuthentication(System.getenv("GMAIL_USERNAME"), System.getenv("GMAIL_PASSWORD"))
                }
            })
        }
    }
}
