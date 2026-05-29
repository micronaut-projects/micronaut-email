package io.micronaut.email.mailpit.client

import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@Property(name = "micronaut.http.services.mailpit.url", value = "http://localhost:8025")
@MicronautTest(startApplication = false)
internal class MailpitClientInjectionTest {

    @Test
    fun mailpitClientCanBeInjected(mailpitClient: MailpitClient) {
        assertNotNull(mailpitClient)
    }
}
