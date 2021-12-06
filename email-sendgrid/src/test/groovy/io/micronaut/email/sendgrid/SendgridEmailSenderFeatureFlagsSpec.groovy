package io.micronaut.email.sendgrid

import com.sendgrid.Response
import io.micronaut.context.annotation.Property
import io.micronaut.email.EmailSender
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "sendgrid.api-key", value = "xxx")
@MicronautTest(startApplication = false)
class SendgridEmailSenderFeatureFlagsSpec extends Specification {
    @Inject
    EmailSender<Response> emailSender;

    void "Send Grid supports attachments"() {
        expect:
        emailSender.isSendingAttachmentsSupported()
    }

    void "Send Grid supports tracking"() {
        !emailSender.isTrackingLinksSupported()
    }
}
