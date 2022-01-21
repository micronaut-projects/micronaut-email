package io.micronaut.email.mailjet


import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.test.MailTestUtils
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Requires
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@MicronautTest(startApplication = false)
class MailjetEmailSenderSpec extends Specification {

    @Inject
    EmailSender emailSender

    @Requires({env["MAILJET_API_KEY"] && env["MAILJET_API_SECRET"] && env["GMAIL_USERNAME"] && env["GMAIL_PASSWORD"] && ((!(env['CI'] as boolean) == false) || ((env['CI'] as boolean) && jvm.isJava11()))})
    void "Functional test of Mailjet integration"() {
        given:
        String subject = "[Mailjet] Test" + UUID.randomUUID().toString()
        String gmail = System.getenv("GMAIL_USERNAME")
        when:
        emailSender.send(Email.builder()
                .from(gmail)
                .to(gmail)
                .subject(subject)
                .text("Hello world"))
        then:
        new PollingConditions(initialDelay: 10, delay: 20, timeout: 300).eventually {
            1 == MailTestUtils.countAndDeleteInboxEmailsBySubject(subject)
        }
    }
}
