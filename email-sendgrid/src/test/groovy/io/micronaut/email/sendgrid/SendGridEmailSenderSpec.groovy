package io.micronaut.email.sendgrid

import io.micronaut.email.EmailSender
import io.micronaut.email.Email
import io.micronaut.email.test.CiUtils
import io.micronaut.email.test.MailTestUtils
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Requires
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@MicronautTest(startApplication = false)
class SendGridEmailSenderSpec extends Specification {

    @Inject
    EmailSender emailSender

    @Requires({
                env['SENDGRID_ENABLED'] &&
                env["SENDGRID_API_KEY"] &&
                env["GMAIL_USERNAME"] &&
                env["GMAIL_PASSWORD"] &&
                (!CiUtils.runningOnCI() || (CiUtils.runningOnCI() && jvm.isJava11()))
    })
    void "Functional test of SendGrid integration"() {
        given:
        String subject = "[Sendgrid] Test" + UUID.randomUUID().toString()
        String gmail = System.getenv("GMAIL_USERNAME")
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
    }
}
