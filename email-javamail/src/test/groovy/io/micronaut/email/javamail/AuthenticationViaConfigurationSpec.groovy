package io.micronaut.email.javamail

import io.micronaut.context.ApplicationContext
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.javamail.sender.authentication.JavaMailAuthenticationConfiguration
import io.micronaut.email.test.MailTestUtils
import spock.lang.Requires
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@Requires({ env["GMAIL_USERNAME"] && env["GMAIL_PASSWORD"] })
class AuthenticationViaConfigurationSpec extends Specification {

    void "no authentication via configuration if no config exists"() {
        when:
        ApplicationContext applicationContext = ApplicationContext.run(
                "spec.name": "AuthenticationViaConfigurationSpec",
                'javamail.properties': [
                        'mail.smtp.host'               : "smtp.gmail.com",
                        'mail.smtp.socketFactory.port' : "465",
                        'mail.smtp.socketFactory.class': "javax.net.ssl.SSLSocketFactory",
                        "mail.smtp.port"               : "465"
                ]
        )

        then:
        !applicationContext.findBean(JavaMailAuthenticationConfiguration).present
        !applicationContext.findBean(Authenticator).present

        cleanup:
        applicationContext.close()
    }

    void "no authentication via configuration if disabled"() {
        when:
        ApplicationContext applicationContext = ApplicationContext.run(
                "spec.name": "AuthenticationViaConfigurationSpec",
                'javamail.properties': [
                        'mail.smtp.host'               : "smtp.gmail.com",
                        'mail.smtp.socketFactory.port' : "465",
                        'mail.smtp.socketFactory.class': "javax.net.ssl.SSLSocketFactory",
                        "mail.smtp.port"               : "465"
                ],
                'javamail.authentication.enabled': 'false',
                'javamail.authentication.username': System.getenv('GMAIL_USERNAME'),
                'javamail.authentication.password': System.getenv('GMAIL_PASSWORD'),
        )

        then:
        !applicationContext.findBean(JavaMailAuthenticationConfiguration).present
        !applicationContext.findBean(Authenticator).present

        cleanup:
        applicationContext.close()
    }

    void "authentication via configuration"() {
        when:
        ApplicationContext applicationContext = ApplicationContext.run(
                "spec.name": "AuthenticationViaConfigurationSpec",
                'javamail.properties': [
                        'mail.smtp.host'               : "smtp.gmail.com",
                        'mail.smtp.socketFactory.port' : "465",
                        'mail.smtp.socketFactory.class': "javax.net.ssl.SSLSocketFactory",
                        "mail.smtp.port"               : "465"
                ],
                'javamail.authentication.username': System.getenv('GMAIL_USERNAME'),
                'javamail.authentication.password': System.getenv('GMAIL_PASSWORD'),
        )

        EmailSender emailSender = applicationContext.getBean(EmailSender)

        String subject = "[Javax Mail] Test" + UUID.randomUUID().toString()
        String gmail = System.getenv("GMAIL_USERNAME")

        then:
        applicationContext.findBean(JavaMailAuthenticationConfiguration).present
        applicationContext.findBean(Authenticator).present

        when:
        emailSender.send(Email.builder()
                .from(gmail)
                .to(gmail)
                .subject(subject)
                .body("Hello world"))
        then:
        new PollingConditions(initialDelay: 10, delay: 20, timeout: 600, factor: 1.25).eventually {
            1 == MailTestUtils.countAndDeleteInboxEmailsBySubject(subject)
        }

        cleanup:
        applicationContext.close()
    }
}
