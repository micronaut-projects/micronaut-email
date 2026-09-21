from typing import Annotated

from jakarta.inject import Inject
from micronaut.context import BeanContext
from micronaut.email.template import TemplateBodyDecorator
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test


@MicronautTest(startApplication=False)
class TemplateBodyDecoratorTest:
    bean_context: Annotated[BeanContext, Inject]

    @Test
    def bean_of_type_template_body_decorator_exists(self):
        assert self.bean_context.containsBean(TemplateBodyDecorator)
