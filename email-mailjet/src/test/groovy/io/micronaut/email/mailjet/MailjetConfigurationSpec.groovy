package io.micronaut.email.mailjet

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class MailjetConfigurationSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "by default there is no bean of type MailjetConfiguration"() {
        expect:
        !beanContext.containsBean(MailjetConfiguration)
    }
}
