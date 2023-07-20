package io.micronaut.email.sendgrid

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.email.Email
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class SendgridEmailComposerSpec extends Specification {

    @Inject
    SendgridEmailComposer sendgridEmailComposer

    void "from, to, only the last reply to and subject are put to the mime message"() {
        given:
        def from = "sender@example.com"
        def to = "receiver@example.com"
        def replyTo1 = "sender.reply.to.one@example.com"
        def replyTo2 = "sender.reply.to.two@example.com"
        def subject = "Apple Music"
        def body = "Lore ipsum body"

        Email email = Email.builder()
                .from(from)
                .to(to)
                .replyTo(replyTo1)
                .replyTo(replyTo2)
                .subject(subject)
                .body(body)
                .build()
        when:
        def request = sendgridEmailComposer.compose(email)
        def map = new ObjectMapper().readValue(request.body, Map)

        then:
        map["from"]["email"] == from
        map["subject"] == subject
        map["personalizations"][0]["to"][0]["email"] == to
        map["personalizations"][0]["subject"] == subject
        map["content"][0]["type"] == "text/plain"
        map["content"][0]["value"] == body
        map["reply_to"]["email"] == replyTo2
    }
}
