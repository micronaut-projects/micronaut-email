from typing import Annotated

from com.sendgrid import SendGrid
from jakarta.inject import Inject
from micronaut.context.annotation import Property
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test


@Property(name="spec.name", value="SendGridBeanCreatedEventListenerTest")
@Property(name="sendgrid.api-key", value="xxx")
@Property(name="javamail.enabled", value="false")
@MicronautTest(startApplication=False)
class SendGridBeanCreatedEventListenerTest:
    send_grid: Annotated[SendGrid, Inject]

    @Test
    def send_grid_bean_is_customized_by_the_listener(self):
        assert 5000 == self.send_grid.getRateLimitSleep()
