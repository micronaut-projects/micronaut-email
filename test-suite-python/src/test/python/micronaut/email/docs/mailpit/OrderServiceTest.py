from typing import Annotated

from jakarta.inject import Inject
from java.util import UUID
from micronaut.context.annotation import Property
from micronaut.email.configuration import FromConfiguration
from micronaut.email.mailpit.client import MailpitClient
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import AfterAll, Assumptions, Disabled, Test
from org.testcontainers import DockerClientFactory

from micronaut.email.test import Mailpit
from .OrderService import OrderService


# The Mailpit Testcontainers container properties (SMTP host/port and the Mailpit HTTP client URL) are supplied
# by the Java io.micronaut.email.docs.MailpitTestConfigurer application context configurer of the "mailpit"
# environment, see DISABLED_TESTS.md.
@Property(name="spec.name", value="OrderServiceTest")
@Property(name="micronaut.email.from.email", value="info@micronaut.io")
@MicronautTest(startApplication=False, environments=["mailpit"])
class OrderServiceTest:
    order_service: Annotated[OrderService, Inject]
    client: Annotated[MailpitClient, Inject]
    from_configuration: Annotated[FromConfiguration, Inject]

    @AfterAll
    @staticmethod
    def cleanup_spec() -> None:
        Mailpit.close()

    # TODO(python): keyword alias on foreign object
    @Disabled("MailpitMessage.from_ alias is not resolved on the message returned by MailpitClient.getMessage() (Python compiler gap)")
    @Test
    def order_service(self):
        Assumptions.assumeTrue(DockerClientFactory.instance().isDockerAvailable(), "Docker is not available")
        recipient = "example@micronaut.io"
        order_number = str(UUID.randomUUID().toString())
        text = "We have received your order " + order_number + ". You will receive your product soon."
        html = "<html><body><p>" + text + "</p></body></html>"

        self.order_service.send_order_email(recipient, order_number)

        message = self.client.getMessage("latest")

        assert message is not None
        assert message.from_() is not None
        assert message.to() is not None
        sender = message.from_()
        to = message.to()
        assert self.from_configuration.getFrom().getEmail() == sender.address()
        assert [recipient] == [address.address() for address in to]
        assert "Order Number: " + order_number == message.subject()
        assert text == message.text()
        assert html == message.html()
