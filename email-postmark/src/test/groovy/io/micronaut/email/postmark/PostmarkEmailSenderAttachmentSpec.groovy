package io.micronaut.email.postmark

import io.micronaut.email.Attachment
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.test.CiUtils
import io.micronaut.email.test.MailTestUtils
import io.micronaut.email.test.SpreadsheetUtils
import io.micronaut.http.MediaType
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Requires
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@MicronautTest(startApplication = false)
class PostmarkEmailSenderAttachmentSpec extends Specification {

    @Inject
    EmailSender emailSender

    @Requires({
                env['POSTMARK_ENABLED'] &&
                env["POSTMARK_API_TOKEN"] &&
                env["GMAIL_USERNAME"] &&
                env["GMAIL_PASSWORD"] &&
                (!CiUtils.runningOnCI() || (CiUtils.runningOnCI() && jvm.isJava17()))
    })
    void "Functional test for an Email with attachment and Postmark integration"() {
        given:
        String subject = "[Postmark] Attachment Test" + UUID.randomUUID().toString()
        String to = System.getenv("GMAIL_USERNAME")

        when:
        emailSender.send(Email.builder()
                .from("marketing@micronaut.io")
                .to(to)
                .subject(subject)
                .body("Hello world")
                .attachment(Attachment.builder()
                        .filename("monthlyreports.xlsx")
                        .contentType(MediaType.MICROSOFT_EXCEL_OPEN_XML)
                        .content(SpreadsheetUtils.spreadsheet())
                        .build()))
        then:
        new PollingConditions(initialDelay: 10, delay: 20, timeout: 600, factor: 1.25).eventually {
            1 == MailTestUtils.countAndDeleteInboxEmailsBySubject(subject)
        }
    }
}
