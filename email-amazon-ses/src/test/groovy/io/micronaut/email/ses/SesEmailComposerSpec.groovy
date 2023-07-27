package io.micronaut.email.ses


import io.micronaut.email.Email
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import software.amazon.awssdk.services.ses.model.SendEmailRequest
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest
import spock.lang.Specification
import spock.lang.Unroll

@MicronautTest(startApplication = false)
class SesEmailComposerSpec extends Specification {

    @Inject
    SesEmailComposer sesEmailComposer

    @Unroll
    void "test attachments with id: #contentId and disposition: #disposition"(String contentId, String disposition) {
        given:
        String from = "sender@example.com"
        String to = "receiver@example.com"
        String subject = "Apple Music"
        String body = "Lore ipsum body"
        String filename = "my-file.txt"
        String contentType = "text/markdown"
        String content = "hello"
        Email email = Email.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .body(body)
                .attachment { it.filename(filename).id(contentId).disposition(disposition).contentType(contentType).content(content.bytes) }
                .build()
        when:
        SendRawEmailRequest request = sesEmailComposer.compose(email)
        String raw = new String(request.rawMessage.data.asByteArray())
        then:
        raw.contains("From: $from")
        raw.contains("To: $to")
        raw.contains("Subject: $subject")
        raw.contains(body)
        !contentId || raw.contains("Content-ID: $contentId")
        raw.contains("Content-Disposition: $expectedDisposition; filename=$filename")
        raw.contains("Content-Type: $contentType")
        raw.contains(content)
        where:
        contentId     | disposition  | expectedDisposition
        null          | null         | "attachment"
        "my-file"     | null         | "attachment"
        "my-file"     | "inline"     | "inline"
        "my-file"     | "attachment" | "attachment"
        null          | "inline"     | "inline"
        null          | "attachment" | "attachment"
    }

    void "from, to and subject are put to the mime message"() {
        given:
        def from = "sender@example.com"
        def to = "receiver@example.com"
        def subject = "Apple Music"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .body("Lore ipsum body")
                .build()
        when:
        def request = sesEmailComposer.compose(email) as SendEmailRequest

        then:
        from == request.source()
        [to] == request.destination().toAddresses().toList()
        subject == request.message().subject().data()
        !request.destination().ccAddresses()
        !request.destination().bccAddresses()
    }

    void "from, to, cc and subject are put to the mime message"() {
        given:
        def from = "sender@example.com"
        def to = "receiver@example.com"
        def cc = "receiver.cc@example.com"
        def subject = "Apple Music"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .cc(cc)
                .subject(subject)
                .body("Lore ipsum body")
                .build()
        when:
        def request = sesEmailComposer.compose(email) as SendEmailRequest

        then:
        from == request.source()
        [to] == request.destination().toAddresses().toList()
        [cc] == request.destination().ccAddresses().toList()
        subject == request.message().subject().data()
        !request.destination().bccAddresses()
    }

    void "from, to, bcc and subject are put to the mime message"() {
        given:
        def from = "sender@example.com"
        def to = "receiver@example.com"
        def bcc = "receiver.bcc@example.com"
        def subject = "Apple Music"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .bcc(bcc)
                .subject(subject)
                .body("Lore ipsum body")
                .build()
        when:
        def request = sesEmailComposer.compose(email) as SendEmailRequest

        then:
        from == request.source()
        [to] == request.destination().toAddresses().toList()
        [bcc] == request.destination().bccAddresses().toList()
        subject == request.message().subject().data()
        !request.destination().ccAddresses()
    }

    void "from, to, reply to and subject are put to the mime message"() {
        given:
        def from = "sender@example.com"
        def to = "receiver@example.com"
        def replyTo = "sender.reply.to@example.com"
        def subject = "Apple Music"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .replyTo(replyTo)
                .subject(subject)
                .body("Lore ipsum body")
                .build()
        when:
        def request = sesEmailComposer.compose(email) as SendEmailRequest

        then:
        from == request.source()
        [to] == request.destination().toAddresses().toList()
        [replyTo] == request.replyToAddresses()
        subject == request.message().subject().data()
        !request.destination().ccAddresses()
    }
}
