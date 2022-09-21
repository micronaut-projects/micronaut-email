package io.micronaut.email.ses

import io.micronaut.email.Contact
import io.micronaut.email.Email
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import software.amazon.awssdk.services.ses.model.SendEmailRequest
import spock.lang.Specification

@MicronautTest(startApplication = false)
class SesEmailComposerSpec extends Specification {

    @Inject
    SesEmailComposer sesEmailComposer

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

    void "from field should allow including the sender name"() {
        given:
        def from = new Contact("sender@example.com", "John Doe")
        def formattedFrom = "${from.getName()} <${from.getEmail()}>"
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
        formattedFrom == request.source()
        [to] == request.destination().toAddresses().toList()
        subject == request.message().subject().data()
        !request.destination().ccAddresses()
        !request.destination().bccAddresses()
    }
}
