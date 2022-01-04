package io.micronaut.email.javamail

import io.micronaut.context.BeanContext
import io.micronaut.context.exceptions.NoSuchBeanException
import io.micronaut.email.javamail.sender.JavaMailConfiguration
import io.micronaut.email.javamail.sender.MailPropertiesProvider
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class MailPropertiesProviderNotPresentByDefaultSpec extends Specification {
    @Inject
    BeanContext beanContext
    void "a bean of type MailPropertiesProvider is not present by default"() {
        expect:
        beanContext.containsBean(JavaMailConfiguration)

        when:
        beanContext.getBean(MailPropertiesProvider)

        then:
        thrown(NoSuchBeanException)
    }

}
