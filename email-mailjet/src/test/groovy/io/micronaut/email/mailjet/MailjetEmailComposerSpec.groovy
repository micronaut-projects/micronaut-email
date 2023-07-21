package io.micronaut.email.mailjet

import com.fasterxml.jackson.databind.json.JsonMapper
import com.mailjet.client.MailjetRequest
import io.micronaut.email.Email
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class MailjetEmailComposerSpec extends Specification {

    @Inject
    MailjetEmailComposer mailjetEmailComposer

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
        Map map = new JsonMapper().readValue(request.body, Map)

        then:
        map["Messages"][0]["ReplyTo"]["Email"] == replyTo2
        map["Messages"][0]["TextPart"] == body
        map["Messages"][0]["From"]["Email"] == from
        map["Messages"][0]["To"][0]["Email"] == to
        map["Messages"][0]["Subject"] == subject
    }
}
