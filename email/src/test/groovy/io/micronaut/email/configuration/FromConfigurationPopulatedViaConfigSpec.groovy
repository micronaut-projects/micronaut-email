package io.micronaut.email.configuration

import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = 'micronaut.email.from.email', value = 'tcook@apple.com')
@Property(name = 'micronaut.email.from.name', value = 'Tim Cook')
@MicronautTest(startApplication = false)
class FromConfigurationPopulatedViaConfigSpec extends Specification {
    @Inject
    FromConfiguration fromConfiguration

    void "FromConfiguration is populated via config"() {
        expect:
        'tcook@apple.com' == fromConfiguration.getEmail()
        'Tim Cook'  == fromConfiguration.getName()
    }
}
