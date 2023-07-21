package io.micronaut.email.ses


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
        String from = "sender@example.com"
        String to = "receiver@example.com"
        String subject = "Apple Music"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .body("Lore ipsum body")
                .build()
        when:
        SendEmailRequest request = sesEmailComposer.compose(email)

        then:
        from == request.source()
        [to] == request.destination().toAddresses().toList()
        subject == request.message().subject().data()
        !request.destination().ccAddresses()
        !request.destination().bccAddresses()
    }

    void "from, to, cc and subject are put to the mime message"() {
        given:
        String from = "sender@example.com"
        String to = "receiver@example.com"
        String cc = "receiver.cc@example.com"
        String subject = "Apple Music"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .cc(cc)
                .subject(subject)
                .body("Lore ipsum body")
                .build()
        when:
        SendEmailRequest request = sesEmailComposer.compose(email)

        then:
        from == request.source()
        [to] == request.destination().toAddresses().toList()
        [cc] == request.destination().ccAddresses().toList()
        subject == request.message().subject().data()
        !request.destination().bccAddresses()
    }

    void "from, to, bcc and subject are put to the mime message"() {
        given:
        String from = "sender@example.com"
        String to = "receiver@example.com"
        String bcc = "receiver.bcc@example.com"
        String subject = "Apple Music"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .bcc(bcc)
                .subject(subject)
                .body("Lore ipsum body")
                .build()
        when:
        SendEmailRequest request = sesEmailComposer.compose(email)

        then:
        from == request.source()
        [to] == request.destination().toAddresses().toList()
        [bcc] == request.destination().bccAddresses().toList()
        subject == request.message().subject().data()
        !request.destination().ccAddresses()
    }

    void "from, to, reply to and subject are put to the mime message"() {
        given:
        String from = "sender@example.com"
        String to = "receiver@example.com"
        String replyTo = "sender.reply.to@example.com"
        String subject = "Apple Music"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .replyTo(replyTo)
                .subject(subject)
                .body("Lore ipsum body")
                .build()
        when:
        SendEmailRequest request = sesEmailComposer.compose(email)

        then:
        from == request.source()
        [to] == request.destination().toAddresses().toList()
        [replyTo] == request.replyToAddresses()
        subject == request.message().subject().data()
        !request.destination().ccAddresses()
    }

    void "from, to, multiple reply to and subject are put to the mime message"() {
        given:
        String from = "sender@example.com"
        String to = "receiver@example.com"
        String replyTo1 = "sender.reply.to.one@example.com"
        String replyTo2 = "sender.reply.to.two@example.com"
        String subject = "Apple Music"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .replyTo(replyTo1)
                .replyTo(replyTo2)
                .subject(subject)
                .body("Lore ipsum body")
                .build()
        when:
        SendEmailRequest request = sesEmailComposer.compose(email)

        then:
        from == request.source()
        [to] == request.destination().toAddresses().toList()
        [replyTo1, replyTo2] == request.replyToAddresses()
        subject == request.message().subject().data()
        !request.destination().ccAddresses()
    }
}
