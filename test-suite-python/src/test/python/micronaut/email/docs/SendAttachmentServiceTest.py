from typing import Annotated

from jakarta.inject import Inject
from micronaut.context.annotation import Property
from micronaut.email import BodyType
from micronaut.email.mock import MockEmailSender
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

from .SendAttachmentService import SendAttachmentService


@Property(name="mock.emailsender", value="true")
@Property(name="javamail.enabled", value="false")
@MicronautTest(startApplication=False)
class SendAttachmentServiceTest:
    send_attachment_service: Annotated[SendAttachmentService, Inject]
    email_sender: Annotated[MockEmailSender, Inject]

    @Test
    def transactional_text_email_is_correctly_built(self):
        # when:
        self.send_attachment_service.send_report()
        # then:
        assert 1 == self.email_sender.getEmails().size()
        email = self.email_sender.getEmails().get(0)
        assert "sender@example.com" == email.getFrom().getEmail()
        assert email.getFrom().getName() is None
        assert 1 == email.getTo().size()
        assert "john@example.com" == email.getTo().stream().findFirst().get().getEmail()
        assert email.getTo().stream().findFirst().get().getName() is None
        assert email.getCc() is None
        assert email.getBcc() is None
        assert "Monthly reports" == email.getSubject()
        assert email.getBody() is not None
        assert email.getBody().get(BodyType.TEXT).isPresent()
        assert "Attached Monthly reports" == email.getBody().get(BodyType.TEXT).get()
        assert email.getBody().get(BodyType.HTML).isPresent()
        assert "<html><body><strong>Attached Monthly reports</strong>.</body></html>" == email.getBody().get(BodyType.HTML).get()
        assert email.getAttachments() is not None
        assert "reports.xlsx" == email.getAttachments().get(0).getFilename()
        assert "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" == email.getAttachments().get(0).getContentType()
        assert email.getAttachments().get(0).getContent() is not None
