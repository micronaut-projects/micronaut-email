package io.micronaut.email.docs.sendgrid;

import com.sendgrid.SendGrid;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "spec.name", value = "SendGridBeanCreatedEventListenerTest")
@Property(name = "sendgrid.api-key", value = "xxx")
@Property(name = "javamail.enabled", value = StringUtils.FALSE)
@MicronautTest(startApplication = false)
class SendGridBeanCreatedEventListenerTest {

    @Inject
    SendGrid sendGrid;

    @Test
    void sendGridBeanIsCustomizedByTheListener() {
        assertEquals(5000, sendGrid.getRateLimitSleep());
    }
}
