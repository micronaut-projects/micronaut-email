package io.micronaut.email.postmark

import com.postmarkapp.postmark.client.data.model.message.Message
import io.micronaut.email.Email
import io.micronaut.email.TrackLinks
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class PostmarkEmailComposerSpec extends Specification {

    @Inject
    PostmarkEmailComposer postmarkEmailComposer

    @MockBean
    PostmarkConfiguration postmarkConfiguration = Stub() {
        getTrackLinks() >> TrackLinks.DO_NOT_TRACK
    }

    void "from, to, only the last reply to and subject are put to the message"() {
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
        Message message = postmarkEmailComposer.compose(email)

        then:
        message.from == from
        message.subject == subject
        message.to == "\"$to\""
        message.textBody == body
        message.replyTo == replyTo2
    }
}
