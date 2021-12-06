package io.micronaut.email.postmark

import com.wildbit.java.postmark.client.data.model.message.MessageResponse
import io.micronaut.context.annotation.Property
import io.micronaut.email.EmailSender
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "postmark.api-token", value = "xxx")
@MicronautTest(startApplication = false)
class PostmarkEmailSenderFeatureFlagsSpec extends Specification {

    @Inject
    EmailSender<MessageResponse> emailSender;

    void "postmark supports attachments"() {
        expect:
        emailSender.isSendingAttachmentsSupported()
    }

    void "postmark supports tracking"() {
        emailSender.isTrackingLinksSupported()
    }
}
