package io.micronaut.email.mailjet

import com.mailjet.client.MailjetRequest
import io.micronaut.email.Email
import io.micronaut.json.JsonMapper
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import spock.lang.Unroll

@MicronautTest(startApplication = false)
class MailjetEmailComposerSpec extends Specification {

    @Inject
    MailjetEmailComposer mailjetEmailComposer

    @Inject
    JsonMapper jsonMapper

    void "from, to, only the last reply to and subject are put to the request"() {
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
        MailjetRequest request = mailjetEmailComposer.compose(email)
        Map map = jsonMapper.readValue(request.body, Map)

        then:
        map["Messages"][0]["ReplyTo"]["Email"] == replyTo2
        map["Messages"][0]["TextPart"] == body
        map["Messages"][0]["From"]["Email"] == from
        map["Messages"][0]["To"][0]["Email"] == to
        map["Messages"][0]["Subject"] == subject
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
                .body(body)
                .attachment { it.filename(filename).id(contentId).disposition(disposition).contentType(contentType).content(content.bytes) }
                .build()
        when:
        MailjetRequest request = mailjetEmailComposer.compose(email)
        Map map = jsonMapper.readValue(request.body, Map)
        then:
        map["Messages"][0]["TextPart"] == body
        map["Messages"][0]["From"]["Email"] == from
        map["Messages"][0]["To"][0]["Email"] == to
        map["Messages"][0]["Subject"] == subject
        map["Messages"][0][expectedDisposition][0]["Base64Content"] == new String(Base64.encoder.encode(content.bytes))
        map["Messages"][0][expectedDisposition][0]["ContentType"] == contentType
        map["Messages"][0][expectedDisposition][0]["Filename"] == filename
        !contentId || map["Messages"][0][expectedDisposition][0]["ContentID"] == contentId
        where:
        contentId     | disposition  | expectedDisposition
        null          | null         | "Attachments"
        "my-file"     | null         | "Attachments"
        "my-file"     | "inline"     | "InlinedAttachments"
        "my-file"     | "attachment" | "Attachments"
        null          | "inline"     | "InlinedAttachments"
        null          | "attachment" | "Attachments"
    }
}
