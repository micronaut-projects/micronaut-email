package io.micronaut.email.javaxemail

import io.micronaut.email.Attachment
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.test.MailTestUtils
import io.micronaut.email.test.SpreadsheetUtils
import io.micronaut.http.MediaType
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Requires
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@MicronautTest(startApplication = false)
class JavaxMailEmailSenderAttachmentSpec extends Specification {

    @Inject
    EmailSender emailSender

    @Inject
    SessionProvider sessionProvider

    @Inject
    MailPropertiesProvider mailPropertiesProvider

    @Requires({env["GMAIL_USERNAME"] && env["GMAIL_PASSWORD"]})
    void "Functional test of Email with Attachment for SES integration"() {
        given:
        String subject = "[Javax Mail] Attachment Test"
        String gmail = System.getenv("GMAIL_USERNAME")

        expect:
        mailPropertiesProvider instanceof GmailMailPropertiesProvider
        sessionProvider instanceof GmailSessionProvider
        emailSender instanceof JavaxEmailSender

        when:
        emailSender.send(Email.builder()
                .from(gmail)
                .to(gmail)
                .subject(subject)
                .text("Hello world")
                .attachment(Attachment.builder()
                        .filename("monthlyreports.xlsx")
                        .contentType(MediaType.MICROSOFT_EXCEL_OPEN_XML)
                        .content(SpreadsheetUtils.spreadsheet())
                        .build())
                .build())
        then:
        new PollingConditions(timeout: 30).eventually {
            1 == MailTestUtils.countAndDeleteInboxEmailsBySubject(gmail, System.getenv("GMAIL_PASSWORD"), subject)
        }
    }
}
