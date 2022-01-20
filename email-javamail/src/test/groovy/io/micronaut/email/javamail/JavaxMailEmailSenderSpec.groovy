package io.micronaut.email.javamail

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Property
import io.micronaut.core.annotation.NonNull
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.javamail.sender.MailPropertiesProvider
import io.micronaut.email.javamail.sender.SessionProvider
import io.micronaut.email.test.MailTestUtils
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import spock.lang.AutoCleanup
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

class JavaxMailEmailSenderSpec extends Specification {
    @AutoCleanup
    @Shared
    ApplicationContext applicationContext = ApplicationContext.run([
            "spec.name": "JavaxMailEmailSenderSpec",
            'javamail.properties': ['mail.smtp.host': "smtp.gmail.com",
                                    'mail.smtp.socketFactory.port': "465",
                                    'mail.smtp.socketFactory.class': "javax.net.ssl.SSLSocketFactory",
                                    'mail.smtp.auth': "true",
                                    "mail.smtp.port": "465"]])

    @Shared
    EmailSender emailSender = applicationContext.getBean(EmailSender)

    @Requires({env["GMAIL_USERNAME"] && env["GMAIL_PASSWORD"] && ((!(env['CI'] as boolean) == false) || ((env['CI'] as boolean) && jvm.isJava11()))})
    void "Functional test of SES integration"() {
        given:
        String subject = "[Javax Mail] Test"
        String gmail = System.getenv("GMAIL_USERNAME")

        when:
        emailSender.send(Email.builder()
                .from(gmail)
                .to(gmail)
                .subject(subject)
                .text("Hello world"))
        then:
        new PollingConditions(timeout: 30).eventually {
            1 == MailTestUtils.countAndDeleteInboxEmailsBySubject(subject)
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
