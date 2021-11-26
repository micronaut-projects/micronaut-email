package io.micronaut.email.postmark

import io.micronaut.context.annotation.Property
import io.micronaut.email.TransactionalEmail
import io.micronaut.email.test.MailTestUtils
import io.micronaut.email.EmailCourier
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Requires
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@MicronautTest(startApplication = false)
class PostmarkEmailCourierSpec extends Specification {

    @Inject
    EmailCourier emailCourier

    @Requires({env["POSTMARK_API_TOKEN"] && env["GMAIL_USERNAME"] && env["GMAIL_PASSWORD"]})
    void "Functional test of postmark integration"() {
        given:
        String subject = "[Postmark] Test"
        String to = System.getenv("GMAIL_USERNAME")
        when:
        emailCourier.send(TransactionalEmail.builder()
                .from("marketing@micronaut.io")
                .to(to)
                .subject(subject).text("Hello world")
                .build())
        then:
        new PollingConditions(timeout: 20).eventually {
            1 == MailTestUtils.countAndDeleteInboxEmailsBySubject(to, System.getenv("GMAIL_PASSWORD"), subject)
        }
    }
}
