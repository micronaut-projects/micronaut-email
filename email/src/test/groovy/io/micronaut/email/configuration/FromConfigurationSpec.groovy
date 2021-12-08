package io.micronaut.email.configuration

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class FromConfigurationSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "by default no bean of type from configuration exists"() {
        expect:
        !beanContext.containsBean(FromConfiguration)
    }
}
