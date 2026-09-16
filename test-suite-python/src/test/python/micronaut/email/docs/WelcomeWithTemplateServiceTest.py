from typing import Annotated

from jakarta.inject import Inject
from micronaut.context.annotation import Property
from micronaut.email import BodyType
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Disabled, Test

from .MockEmailSender import MockEmailSender
from .WelcomeWithTemplateService import WelcomeWithTemplateService


@Property(name="mock.emailsender", value="true")
@Property(name="javamail.enabled", value="false")
@MicronautTest(startApplication=False)
class WelcomeWithTemplateServiceTest:
    welcome_service: Annotated[WelcomeWithTemplateService, Inject]
    email_sender: Annotated[MockEmailSender, Inject]

    # TODO(python): keyword alias on foreign object
    @Disabled("Email.Builder.from_ alias is not resolved on the builder returned by Email.builder() (Python compiler gap)")
    @Test
    def transactional_email_is_correctly_built(self):
        # given
        message = "Hello dear Micronaut user"
        copyright = "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED"
        address = "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141"
        # when:
        self.welcome_service.send_welcome_email()
        # then:
        assert 1 == len(self.email_sender.get_emails())

        email = self.email_sender.get_emails()[0]
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

        text = email.getBody().get(BodyType.TEXT).get()
        assert message in text
        assert copyright in text
        assert address in text
        assert email.getBody().get(BodyType.HTML).isPresent()

        html = email.getBody().get(BodyType.HTML).get()
        assert "<h2 class=\"cit\">" + message + "</h2>" in html
        assert "<div>" + copyright + "</div>" in html
        assert "<div>" + address + "</div>" in html
