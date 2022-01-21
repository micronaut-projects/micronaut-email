package io.micronaut.email.docs

import io.micronaut.email.BodyType
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class SendAttachmentServiceSpec extends Specification {

    @Inject
    SendAttachmentService sendAttachmentService

    @Inject
    MockEmailSender emailSender

    void "Email is correctly built"() {
        when:
        sendAttachmentService.sendReport()
        then:
        emailSender.emails.size()
        'sender@example.com' == emailSender.emails[0].from.email
        !emailSender.emails[0].from.name
        1 == emailSender.emails[0].to.size()
        'john@example.com' == emailSender.emails[0].to[0].email
        !emailSender.emails[0].to[0].name
        !emailSender.emails[0].cc
        !emailSender.emails[0].bcc
        "Monthly reports" == emailSender.emails[0].subject
        emailSender.emails[0].body
        emailSender.emails[0].body.get(BodyType.TEXT).isPresent()
        "Attached Monthly reports" == emailSender.emails[0].body.get(BodyType.TEXT).get()
        emailSender.emails[0].body.get(BodyType.HTML).isPresent()
        "<html><body><strong>Attached Monthly reports</strong>.</body></html>" == emailSender.emails[0].body.get(BodyType.HTML).get()
        emailSender.emails[0].attachments
        "reports.xlsx" == emailSender.emails[0].attachments.first().filename
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" == emailSender.emails[0].attachments.first().contentType
        emailSender.emails[0].attachments.first().content
    }
}
