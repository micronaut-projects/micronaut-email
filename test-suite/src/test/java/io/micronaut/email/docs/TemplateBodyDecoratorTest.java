package io.micronaut.email.docs;

import io.micronaut.context.BeanContext;
import io.micronaut.email.template.TemplateBodyDecorator;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class TemplateBodyDecoratorTest {

    @Inject
    BeanContext beanContext;

    @Test
    void beanOfTypeTemplateBodyDecoratorExists() {
        assertTrue(beanContext.containsBean(TemplateBodyDecorator.class));
    }
}
