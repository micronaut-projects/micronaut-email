package io.micronaut.email.docs;

import io.micronaut.context.BeanContext;
import io.micronaut.email.template.EmailTemplateSender;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class EmailTemplateSenderTest {

    @Inject
    BeanContext beanContext;

    @Test
    void beanOfTypeEmailTemplateSenderExists() {
        assertTrue(beanContext.containsBean(EmailTemplateSender.class));
        assertTrue(beanContext.containsBean(EmailTemplateSender.class, Qualifiers.byName("mock")));
    }
}
