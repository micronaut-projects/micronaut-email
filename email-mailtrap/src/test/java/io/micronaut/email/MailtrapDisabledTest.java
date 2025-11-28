package io.micronaut.email;

import io.mailtrap.client.MailtrapClient;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "mailtrap.enabled", value = StringUtils.FALSE)
@Property(name = "mailtrap.token", value = "xxx")
@MicronautTest(startApplication = false)
class MailtrapDisabledTest {

    @Inject
    BeanContext beanContext;

    @Test
    void testMailtrapConfiguration() {
        assertFalse(beanContext.containsBean(MailtrapClient.class));
    }

}
