from typing import Annotated

from jakarta.inject import Inject
from jakarta.mail import Session
from jakarta.mail.internet import MimeMessage
from java.util import Properties
from micronaut.context.annotation import Property
from micronaut.email import BodyType
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Disabled, Test

from .MockEmailSender import MockEmailSender
from .CustomizedJavaMailService import CustomizedJavaMailService


@MicronautTest(startApplication=False)
@Property(name="mock.emailsender", value="true")
@Property(name="javamail.enabled", value="false")
class CustomizedJavaMailServiceTest:
    customized_java_mail_service: Annotated[CustomizedJavaMailService, Inject]
    email_sender: Annotated[MockEmailSender, Inject]

    # TODO(python): keyword alias on foreign object
    @Disabled("Email.Builder.from_ alias is not resolved on the builder returned by Email.builder() (Python compiler gap)")
    @Test
    def transactional_html_email_is_correctly_built(self):
        # when:
        self.customized_java_mail_service.send_customized_email()

        # then:
        assert 1 == len(self.email_sender.get_emails())
        email = self.email_sender.get_emails()[0]
        consumer = self.email_sender.get_requests()[0]
        assert "sender@example.com" == email.getFrom().getEmail()
        assert email.getFrom().getName() is None
        assert 1 == email.getTo().size()
        assert "john@example.com" == email.getTo().stream().findFirst().get().getEmail()
        assert email.getTo().stream().findFirst().get().getName() is None
        assert email.getCc() is None
        assert email.getBcc() is None
        assert "Micronaut test" == email.getSubject()
        assert email.getBody() is not None
        assert email.getBody().get(BodyType.TEXT).isPresent()
        assert "Hello dear Micronaut user" == email.getBody().get(BodyType.TEXT).get()
        assert email.getBody().get(BodyType.HTML).isPresent()
        assert "<html><body><strong>Hello</strong> dear Micronaut user.</body></html>" == email.getBody().get(BodyType.HTML).get()

        # when:
        message = MimeMessage(Session.getInstance(Properties()))
        consumer(message)  # the request consumer is the Python function passed to send(), i.e. a plain callable

        # then:
        header = message.getHeader("List-Unsubscribe")
        assert header is not None
        assert 1 == len(header)
        assert "<mailto:list@host.com?subject=unsubscribe>" == header[0]
