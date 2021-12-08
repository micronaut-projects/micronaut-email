package io.micronaut.email.configuration

import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = 'micronaut.email.from.email', value = 'tcook@apple.com')
@MicronautTest(startApplication = false)
class FromConfigurationNameOptionalSpec extends Specification {

    @Inject
    FromConfiguration fromConfiguration

    void "If you specify micronaut.email.from.email a bean of type FromConfiguration is registered"() {
        expect:
        'tcook@apple.com' == fromConfiguration.getEmail()
        !fromConfiguration.getName()
    }
}
