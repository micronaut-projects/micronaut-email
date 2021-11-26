package io.micronaut.email.sendgrid

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.email.EmailSender
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "sendgrid.api-key", value = "xxx")
@MicronautTest(startApplication = false)
class SendGridEmailSenderNamedSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "SendGridEmailCourier is annotated with @Named"() {
        expect:
        beanContext.containsBean(EmailSender, Qualifiers.byName("sendgrid"))
    }
}
