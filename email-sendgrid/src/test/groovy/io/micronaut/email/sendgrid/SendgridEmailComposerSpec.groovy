package io.micronaut.email.sendgrid

import com.sendgrid.Request
import io.micronaut.json.JsonMapper
import io.micronaut.email.Email
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import spock.lang.Unroll

@MicronautTest(startApplication = false)
class SendgridEmailComposerSpec extends Specification {

    @Inject
    SendgridEmailComposer sendgridEmailComposer

    @Inject
    JsonMapper jsonMapper

    void "from, to, only the last reply to and subject are put to the mime message"() {
        given:
        String from = "sender@example.com"
        String to = "receiver@example.com"
        String replyTo1 = "sender.reply.to.one@example.com"
        String replyTo2 = "sender.reply.to.two@example.com"
        String subject = "Apple Music"
        String body = "Lore ipsum body"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .replyTo(replyTo1)
                .replyTo(replyTo2)
                .subject(subject)
                .body(body)
                .build()
        when:
        Request request = sendgridEmailComposer.compose(email)
        Map map = jsonMapper.readValue(request.body, Map)

        then:
        map["from"]["email"] == from
        map["subject"] == subject
        map["personalizations"][0]["to"][0]["email"] == to
        map["personalizations"][0]["subject"] == subject
        map["content"][0]["type"] == "text/plain"
        map["content"][0]["value"] == body
        map["reply_to"]["email"] == replyTo2
    }

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
                .body("Lore ipsum body")
                .attachment {it.filename(filename).id(contentId).disposition(disposition).contentType(contentType).content(content.bytes) }
                .build()
        when:
        Request request = sendgridEmailComposer.compose(email)
        Map map = jsonMapper.readValue(request.body, Map)
        then:
        map["from"]["email"] == from
        map["subject"] == subject
        map["personalizations"][0]["to"][0]["email"] == to
        map["personalizations"][0]["subject"] == subject
        map["content"][0]["type"] == "text/plain"
        map["content"][0]["value"] == body
        map["attachments"][0]["content"] == new String(Base64.encoder.encode(content.bytes))
        map["attachments"][0]["type"] == contentType
        map["attachments"][0]["filename"] == filename
        map["attachments"][0]["disposition"] == disposition
        map["attachments"][0]["content_id"] == contentId
        where:
        contentId     | disposition
        null          | null
        "my-file"     | null
        "my-file"     | "inline"
        "my-file"     | "attachment"
        null          | "inline"
        null          | "attachment"
    }
}
