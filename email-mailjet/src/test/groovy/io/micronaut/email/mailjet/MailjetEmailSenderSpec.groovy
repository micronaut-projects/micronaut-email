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

    @Requires({env["MAILJET_API_KEY"] && env["MAILJET_API_SECRET"] && env["GMAIL_USERNAME"] && env["GMAIL_PASSWORD"]})
    void "Functional test of Mailjet integration"() {
        given:
        String subject = "[Mailjet] Test"
        String gmail = System.getenv("GMAIL_USERNAME")
        when:
        emailSender.send(Email.builder()
                .from(gmail)
                .to(gmail)
                .subject(subject)
                .text("Hello world"))
        then:
        new PollingConditions(timeout: 30).eventually {
            1 == MailTestUtils.countAndDeleteInboxEmailsBySubject(gmail, System.getenv("GMAIL_PASSWORD"), subject)
        }
    }
}
