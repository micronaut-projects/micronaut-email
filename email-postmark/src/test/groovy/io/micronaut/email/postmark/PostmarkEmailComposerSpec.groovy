package io.micronaut.email.postmark

import com.postmarkapp.postmark.client.data.model.message.Message
import io.micronaut.email.Email
import io.micronaut.email.TrackLinks
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import spock.lang.Unroll

@MicronautTest(startApplication = false)
class PostmarkEmailComposerSpec extends Specification {

    @Inject
    PostmarkEmailComposer postmarkEmailComposer

    @MockBean
    PostmarkConfiguration postmarkConfiguration = Stub() {
        getTrackLinks() >> TrackLinks.DO_NOT_TRACK
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
        Message message = postmarkEmailComposer.compose(email)
        then:
        message.from == from
        message.subject == subject
        message.to == "\"$to\""
        message.textBody == body
        message.attachments[0]["ContentType"] == contentType
        message.attachments[0]["Content"] == new String(Base64.encoder.encode(content.bytes))
        message.attachments[0]["ContentId"] == contentId
        message.attachments[0]["Name"] == filename
        // Postmark disregards content disposition
        where:
        contentId | disposition
        null      | null
        "my-file" | null
        "my-file" | "inline"
        "my-file" | "attachment"
        null      | "inline"
        null      | "attachment"
    }
}
