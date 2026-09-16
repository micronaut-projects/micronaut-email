from typing import Annotated

import java
from jakarta.inject import Inject
from micronaut.context import BeanContext
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

# TODO(python): java.type needed because EmailSender is passed to BeanContext.containsBean(Class) as a runtime type
# argument; the imported interface (`from micronaut.email import EmailSender`) is a Python wrapper that Java rejects.
EmailSender = java.type("io.micronaut.email.EmailSender")


@MicronautTest(startApplication=False)
class EmailSenderTest:
    bean_context: Annotated[BeanContext, Inject]

    @Test
    def bean_of_type_email_sender_exists(self):
        assert self.bean_context.containsBean(EmailSender)
