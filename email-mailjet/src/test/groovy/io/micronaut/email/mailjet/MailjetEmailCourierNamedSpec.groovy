package io.micronaut.email.mailjet

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.email.EmailCourier
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "mailjet.api-key", value = "xxx")
@MicronautTest(startApplication = false)
class MailjetEmailCourierNamedSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "MailjetEmailCourier is annotated with @Named"() {
        expect:
        beanContext.containsBean(EmailCourier, Qualifiers.byName("mailjet"))
    }
}
