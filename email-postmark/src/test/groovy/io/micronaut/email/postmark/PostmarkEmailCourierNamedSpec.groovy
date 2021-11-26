package io.micronaut.email.postmark

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.email.EmailCourier
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "postmark.api-token", value = "xxx")
@MicronautTest(startApplication = false)
class PostmarkEmailCourierNamedSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "PostmarkEmailCourier is annotated with @Named"() {
        expect:
        beanContext.containsBean(EmailCourier, Qualifiers.byName("postmark"))
    }
}
