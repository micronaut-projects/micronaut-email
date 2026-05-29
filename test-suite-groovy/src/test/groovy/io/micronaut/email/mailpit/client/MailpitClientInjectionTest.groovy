package io.micronaut.email.mailpit.client

import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = 'micronaut.http.services.mailpit.url', value = 'http://localhost:8025')
@MicronautTest(startApplication = false)
class MailpitClientInjectionTest extends Specification {

    @Inject
    MailpitClient mailpitClient

    void "mailpit client can be injected"() {
        expect:
        mailpitClient
    }
}
