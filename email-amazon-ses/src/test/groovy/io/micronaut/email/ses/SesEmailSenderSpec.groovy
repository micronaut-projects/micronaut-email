package io.micronaut.email.ses

import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.test.CiUtils
import io.micronaut.email.test.MailTestUtils
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Requires
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@MicronautTest(startApplication = false)
class SesEmailSenderSpec extends Specification {

    @Inject
    EmailSender<?, ?> emailSender

    @Requires({
        env["AWS_REGION"] &&
                env["AWS_ACCESS_KEY_ID"] &&
                env["AWS_SECRET_ACCESS_KEY"] &&
                env["GMAIL_USERNAME"] &&
                env["GMAIL_PASSWORD"] && (!CiUtils.runningOnCI() || (CiUtils.runningOnCI() && jvm.isJava11())) })
    void "Functional test of SES integration"() {
        given:
        String subject = "[SES] Test" + UUID.randomUUID().toString()
        String gmail = System.getenv("GMAIL_USERNAME")
        when:
        emailSender.send(Email.builder()
                .from(gmail)
                .to(gmail)
                .subject(subject)
                .text("Hello world"))
        then:
        new PollingConditions(initialDelay: 10, delay: 20, timeout: 180).eventually {
            1 == MailTestUtils.countAndDeleteInboxEmailsBySubject(subject)
        }
    }
}
