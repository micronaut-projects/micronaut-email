package io.micronaut.email.resend;

import io.micronaut.context.ApplicationContext;
import io.micronaut.email.EmailSender;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResendConfigurationTest {

    @Test
    void apiKeyIsBound() {
        try (ApplicationContext ctx = ApplicationContext.run(Map.of("resend.api-key", "re_xxx"))) {
            ResendConfiguration configuration = ctx.getBean(ResendConfiguration.class);

            assertEquals("re_xxx", configuration.getApiKey());
            assertTrue(configuration.isEnabled());
        }
    }

    @Test
    void integrationRequiresAnApiKey() {
        try (ApplicationContext ctx = ApplicationContext.run()) {
            assertFalse(ctx.containsBean(ResendConfiguration.class));
            assertFalse(ctx.containsBean(ResendEmailSender.class));
            assertFalse(ctx.containsBean(EmailSender.class));
        }
    }
}
