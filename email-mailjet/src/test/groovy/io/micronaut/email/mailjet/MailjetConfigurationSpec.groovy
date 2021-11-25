package io.micronaut.email.mailjet

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Requires
import spock.lang.Specification

@MicronautTest(startApplication = false)
class MailjetConfigurationSpec extends Specification {

    @Inject
    BeanContext beanContext

    @IgnoreIf({ env[ "MAILJET_API_KEY"]})
    void "by default there is no bean of type MailjetConfiguration"() {
        expect:
        !beanContext.containsBean(MailjetConfiguration)
    }
}
