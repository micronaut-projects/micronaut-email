package io.micronaut.email.sendgrid

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import jakarta.inject.Inject

@Property(name = "sendgrid.enabled", value = StringUtils.FALSE)
@Property(name = "sendgrid.api-key", value = "xxx")
@MicronautTest(startApplication = false)
class SendGridToggeableSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "if you set sendgrid.enabled to false you disable Sendgrid integration"() {
        expect:
        !beanContext.containsBean(SendGridConfiguration)
    }
}
