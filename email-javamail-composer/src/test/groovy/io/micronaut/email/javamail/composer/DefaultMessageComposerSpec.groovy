package io.micronaut.email.javamail.composer

import io.micronaut.email.Email
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.mail.Message
import jakarta.mail.internet.MimeMultipart
import spock.lang.Specification
import spock.lang.Unroll

@MicronautTest(startApplication = false)
class DefaultMessageComposerSpec extends Specification {

    @Inject
    DefaultMessageComposer defaultMessageComposer

    @Unroll
    void "test attachments with id: #contentId and disposition: #disposition"(String contentId, String disposition) {
        given:
        String from = "sender@example.com"
        String to = "receiver@example.com"
        String subject = "Apple Music"
        String filename = "my-file.txt"
        String contentType = "text/markdown"
        String content = "hello"
        Email email = Email.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .body("Lore ipsum body")
                .attachment {it.filename(filename).id(contentId).disposition(disposition).contentType(contentType).content(content.bytes) }
                .build()
        when:
        Message message = defaultMessageComposer.compose(email, null)
        then:
        with(message.content as MimeMultipart) {
            it.contentType.startsWith("multipart/mixed")
            parts.size() == 2
            (parts[1].content as InputStream).text == content
            parts[1].contentType == "text/markdown"
            parts[1].disposition == expectedDisposition
            parts[1].fileName == filename
            parts[1].contentType == contentType
            parts[1].getHeader('Content-ID') == expectedContentId
        }
        where:
        contentId     | disposition  | expectedContentId    | expectedDisposition
        null          | null         | null                 | "attachment"
        "my-file"     | null         | ["my-file"]          | "attachment"
        "my-file"     | "inline"     | ["my-file"]          | "inline"
        "my-file"     | "attachment" | ["my-file"]          | "attachment"
        null          | "inline"     | null                 | "inline"
        null          | "attachment" | null                 | "attachment"
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
        def message = defaultMessageComposer.compose(email, null)

        then:
        [from] == message.from.toList().collect {it.address }
        [to] == message.getRecipients(Message.RecipientType.TO).toList().collect{it.address}
        subject == message.getSubject()
        !message.getRecipients(Message.RecipientType.CC)
        !message.getRecipients(Message.RecipientType.BCC)
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
        def message = defaultMessageComposer.compose(email, null)

        then:
        [from] == message.from.toList().collect {it.address }
        [to] == message.getRecipients(Message.RecipientType.TO).toList().collect{it.address}
        [cc] == message.getRecipients(Message.RecipientType.CC).toList().collect{it.address}
        subject == message.getSubject()
        !message.getRecipients(Message.RecipientType.BCC)
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
        def message = defaultMessageComposer.compose(email, null)

        then:
        [from] == message.from.toList().collect {it.address }
        [to] == message.getRecipients(Message.RecipientType.TO).toList().collect{it.address}
        [bcc] == message.getRecipients(Message.RecipientType.BCC).toList().collect{it.address}
        subject == message.getSubject()
        !message.getRecipients(Message.RecipientType.CC)
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
        def message = defaultMessageComposer.compose(email, null)

        then:
        [from] == message.from.toList().collect {it.address }
        [to] == message.getRecipients(Message.RecipientType.TO).toList().collect{it.address}
        [replyTo] == message.replyTo.toList().collect {it.address }
        subject == message.getSubject()
        !message.getRecipients(Message.RecipientType.CC)
    }
}
