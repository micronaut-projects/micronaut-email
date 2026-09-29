from typing import Annotated

from jakarta.inject import Inject
from jakarta.mail import Session
from jakarta.mail.internet import MimeMessage
from java.util import Properties
from micronaut.context.annotation import Property
from micronaut.email import BodyType
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

from .CustomizedJavaMailService import CustomizedJavaMailService
from .MockEmailSender import MockEmailSender


class MessageHeaderCapture(MimeMessage):
    """Records the header the customizer adds instead of building a real message"""

    def __init__(self, session: Session) -> None:
        super().__init__(session)
        self.header_name: str | None = None
        self.header_value: str | None = None

    def addHeader(self, name: str, value: str) -> None:
        self.header_name = name
        self.header_value = value


@MicronautTest(startApplication=False)
@Property(name="mock.emailsender", value="true")
@Property(name="javamail.enabled", value="false")
class CustomizedJavaMailServiceTest:
    customized_java_mail_service: Annotated[CustomizedJavaMailService, Inject]
    email_sender: Annotated[MockEmailSender, Inject]

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
        message = MessageHeaderCapture(Session.getInstance(Properties()))
        consumer(message)  # the request customizer comes back to Python as the function passed to send()

        # then:
        assert "List-Unsubscribe" == message.header_name
        assert "<mailto:list@host.com?subject=unsubscribe>" == message.header_value
