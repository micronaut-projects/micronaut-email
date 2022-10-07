package io.micronaut.email.docs

import io.micronaut.email.BodyType
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.mail.MessagingException
import jakarta.mail.Session
import jakarta.mail.internet.MimeMessage
import spock.lang.Specification

@MicronautTest(startApplication = false)
class CustomizedJavaMailServiceSpec extends Specification {

    @Inject
    CustomizedJavaMailService customizedJavaMailService

    @Inject
    MockEmailSender emailSender

    void "Email is correctly built"() {
        when:
        customizedJavaMailService.sendCustomizedEmail()

        then:
        emailSender.emails.size()
        with(emailSender.emails[0]) {
            from.email == 'sender@example.com'
            !from.name
            to.size() == 1
            to[0].email == 'john@example.com'
            !to[0].name
            !cc
            !bcc
            subject == "Micronaut test"
            body
            body.get(BodyType.TEXT).isPresent()
            body.get(BodyType.TEXT).get() == "Hello dear Micronaut user"
            body.get(BodyType.HTML).isPresent()
            body.get(BodyType.HTML).get() == "<html><body><strong>Hello</strong> dear Micronaut user.</body></html>"
        }

        when:
        MessageHeaderCapture message = new MessageHeaderCapture();
        emailSender.requests[0].accept(message);

        then:
        message.name == "List-Unsubscribe"
        message.value == "<mailto:list@host.com?subject=unsubscribe>"
    }

    static class MessageHeaderCapture extends MimeMessage {

        String name;
        String value;

        public MessageHeaderCapture() {
            super((Session) null);
        }

        @Override
        public void addHeader(String name, String value) throws MessagingException {
            this.name = name;
            this.value = value;
        }
    }
}
