package io.micronaut.email.sendgrid

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import jakarta.inject.Inject

@MicronautTest(startApplication = false)
class SendGridConfigurationSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "by default there is no bean of type SendGridConfiguration"() {
        expect:
        !beanContext.containsBean(SendGridConfiguration)
    }
}
