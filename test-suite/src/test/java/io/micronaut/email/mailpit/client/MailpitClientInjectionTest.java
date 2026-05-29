package io.micronaut.email.mailpit.client;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Property(name = "micronaut.http.services.mailpit.url", value = "http://localhost:8025")
@MicronautTest(startApplication = false)
class MailpitClientInjectionTest {

    @Test
    void mailpitClientCanBeInjected(MailpitClient mailpitClient) {
        assertNotNull(mailpitClient);
    }
}
