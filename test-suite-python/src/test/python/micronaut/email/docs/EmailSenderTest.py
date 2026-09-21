from typing import Annotated

from jakarta.inject import Inject
from micronaut.context import BeanContext
from micronaut.email import EmailSender
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test


@MicronautTest(startApplication=False)
class EmailSenderTest:
    bean_context: Annotated[BeanContext, Inject]

    @Test
    def bean_of_type_email_sender_exists(self):
        assert self.bean_context.containsBean(EmailSender)
