package io.micronaut.email.docs.sendgrid

import com.sendgrid.SendGrid
import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "spec.name", value = "SendGridBeanCreatedEventListenerTest")
@Property(name = "sendgrid.api-key", value = "xxx")
@Property(name = "javamail.enabled", value = StringUtils.FALSE)
@MicronautTest(startApplication = false)
class SendGridBeanCreatedEventListenerTest extends Specification {

    @Inject
    SendGrid sendGrid

    void "SendGrid bean is customized by the listener"() {
        expect:
        sendGrid.rateLimitSleep == 5000
    }
}
