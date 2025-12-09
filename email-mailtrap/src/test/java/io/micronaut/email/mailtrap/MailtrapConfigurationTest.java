package io.micronaut.email.mailtrap;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "mailtrap.token", value = "xxx")
@MicronautTest(startApplication = false)
class MailtrapConfigurationTest {

    @Test
    void testMailtrapConfiguration(MailtrapConfiguration mailtrapConfiguration) {
        assertEquals("xxx", mailtrapConfiguration.getConfig().build().getToken());
    }
}
