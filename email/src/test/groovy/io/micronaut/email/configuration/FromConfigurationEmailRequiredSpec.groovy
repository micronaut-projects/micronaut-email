package io.micronaut.email.configuration

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = 'micronaut.email.from.name', value = 'Tim Cook')
@MicronautTest(startApplication = false)
class FromConfigurationEmailRequiredSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "if you don't specify micronaut.email.from.email even if you specify micronaut.email.from.email no bean of type FromConfiguration is registered"() {
        expect:
        !beanContext.containsBean(FromConfiguration)
    }
}
