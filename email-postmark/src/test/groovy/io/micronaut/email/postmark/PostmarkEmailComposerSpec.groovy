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

    void "from, to, cc, bcc, reply to, subject and body are populated"() {
        given:
        String from = "sender@example.com"
        String to1 = "to1@example.com"
        String to2 = "to2@example.com"
        String cc1 = "cc1@example.com"
        String cc2 = "cc2@example.com"
        String bcc1 = "bcc1@example.com"
        String bcc2 = "bcc2@example.com"
        String replyTo = "sender.reply.to@example.com"
        String subject = "Apple Music"
        String body = "Lore ipsum body"

        Email email = Email.builder()
                .from(from)
                .to(to1)
                .to(to2)
                .cc(cc1)
                .cc(cc2)
                .bcc(bcc1)
                .bcc(bcc2)
                .replyTo(replyTo)
                .subject(subject)
                .body(body)
                .build()
        when:
        Message message = postmarkEmailComposer.compose(email)

        then:
        message.from == from
        message.to == "\"$to1\",\"$to2\""
        message.cc == "\"$cc1\",\"$cc2\""
        message.bcc == "\"$bcc1\",\"$bcc2\""
        message.replyTo == replyTo
        message.subject == subject
        message.textBody == body
    }
}
