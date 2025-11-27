package io.micronaut.email.sendgrid

import com.sendgrid.SendGrid
import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import io.micronaut.core.annotation.NonNull
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Singleton
import spock.lang.Specification
import jakarta.inject.Inject


@Property(name = "spec.name", value = "SendGridSpec")
@Property(name = "sendgrid.api-key", value = "xxx")
@MicronautTest(startApplication = false)
class SendGridSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "if you set sendgrid.enabled to false you disable Sendgrid integration"() {
        expect:
        beanContext.containsBean(SendGrid)

        when:
        SendGrid sendGrid = beanContext.getBean(SendGrid)

        then:
        sendGrid
        5000 == sendGrid.getRateLimitSleep()
    }
}
