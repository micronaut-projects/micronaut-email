package io.micronaut.email.mailtrap;

import io.mailtrap.client.MailtrapClient;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "mailtrap.token", value = "xxx")
@MicronautTest(startApplication = false)
class MailtrapClientFactoryTest {

    @Inject
    BeanContext beanContext;

    @Test
    void testMailtrapConfiguration() {
        assertTrue(beanContext.containsBean(MailtrapClient.class));
    }

}
