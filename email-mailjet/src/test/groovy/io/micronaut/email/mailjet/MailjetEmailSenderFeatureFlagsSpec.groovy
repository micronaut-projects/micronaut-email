package io.micronaut.email.mailjet

import com.mailjet.client.MailjetResponse
import io.micronaut.context.annotation.Property
import io.micronaut.email.EmailSender
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "mailjet.api-secret", value = "xxx")
@Property(name = "mailjet.api-key", value = "yyyy")
@MicronautTest(startApplication = false)
class MailjetEmailSenderFeatureFlagsSpec extends Specification {

    @Inject
    EmailSender<MailjetResponse> emailSender

    void "Mailjet supports attachments"() {
        expect:
        emailSender.isSendingAttachmentsSupported()
    }

    void "Mailjet supports tracking"() {
        !emailSender.isTrackingLinksSupported()
    }
}
