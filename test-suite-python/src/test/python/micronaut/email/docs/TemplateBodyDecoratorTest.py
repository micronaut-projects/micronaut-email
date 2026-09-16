from typing import Annotated

import java
from jakarta.inject import Inject
from micronaut.context import BeanContext
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

# TODO(python): java.type needed because TemplateBodyDecorator is passed to BeanContext.containsBean(Class) as a runtime
# type argument; the imported interface (`from micronaut.email.template import TemplateBodyDecorator`) is a Python wrapper
# that Java rejects.
TemplateBodyDecorator = java.type("io.micronaut.email.template.TemplateBodyDecorator")


@MicronautTest(startApplication=False)
class TemplateBodyDecoratorTest:
    bean_context: Annotated[BeanContext, Inject]

    @Test
    def bean_of_type_template_body_decorator_exists(self):
        assert self.bean_context.containsBean(TemplateBodyDecorator)
