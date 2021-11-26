package io.micronaut.email.docs;

import io.micronaut.context.BeanContext;
import io.micronaut.email.template.EmailRenderer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class EmailRendererTest {

    @Inject
    BeanContext beanContext;

    @Test
    void beanOfTypeEmailRendererExists() {
        assertTrue(beanContext.containsBean(EmailRenderer.class));
    }
}
