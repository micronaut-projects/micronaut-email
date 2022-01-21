package io.micronaut.email.sendgrid

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import jakarta.inject.Inject

@Property(name = "sendgrid.api-key", value = "xxx")
@MicronautTest(startApplication = false)
class SendGridApiKeySpec extends Specification {

    @Inject
    BeanContext beanContext

    void "if you set sendgrid.enabled to false you disable Sendgrid integration"() {
        expect:
        beanContext.containsBean(SendGridConfiguration)
        'xxx' == beanContext.getBean(SendGridConfiguration).apiKey
    }
}
