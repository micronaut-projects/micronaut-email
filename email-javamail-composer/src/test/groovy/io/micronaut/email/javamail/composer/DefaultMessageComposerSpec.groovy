package io.micronaut.email.javamail.composer

import io.micronaut.email.Email
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.mail.Message
import spock.lang.Specification

@MicronautTest(startApplication = false)
class DefaultMessageComposerSpec extends Specification {

    @Inject
    DefaultMessageComposer defaultMessageComposer

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
        Message message = defaultMessageComposer.compose(email, null)

        then:
        [from] == message.from.toList().collect {it.address }
        [to] == message.getRecipients(Message.RecipientType.TO).toList().collect{it.address}
        subject == message.getSubject()
        !message.getRecipients(Message.RecipientType.CC)
        !message.getRecipients(Message.RecipientType.BCC)
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
        Message message = defaultMessageComposer.compose(email, null)

        then:
        [from] == message.from.toList().collect {it.address }
        [to] == message.getRecipients(Message.RecipientType.TO).toList().collect{it.address}
        [cc] == message.getRecipients(Message.RecipientType.CC).toList().collect{it.address}
        subject == message.getSubject()
        !message.getRecipients(Message.RecipientType.BCC)
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
        Message message = defaultMessageComposer.compose(email, null)

        then:
        [from] == message.from.toList().collect {it.address }
        [to] == message.getRecipients(Message.RecipientType.TO).toList().collect{it.address}
        [bcc] == message.getRecipients(Message.RecipientType.BCC).toList().collect{it.address}
        subject == message.getSubject()
        !message.getRecipients(Message.RecipientType.CC)
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
        Message message = defaultMessageComposer.compose(email, null)

        then:
        [from] == message.from.toList().collect {it.address }
        [to] == message.getRecipients(Message.RecipientType.TO).toList().collect{it.address}
        [replyTo] == message.replyTo.toList().collect {it.address }
        subject == message.getSubject()
        !message.getRecipients(Message.RecipientType.CC)
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
        Message message = defaultMessageComposer.compose(email, null)

        then:
        [from] == message.from.toList().collect {it.address }
        [to] == message.getRecipients(Message.RecipientType.TO).toList().collect{it.address}
        [replyTo1, replyTo2] == message.replyTo.toList().collect {it.address }
        subject == message.getSubject()
        !message.getRecipients(Message.RecipientType.CC)
    }
}
