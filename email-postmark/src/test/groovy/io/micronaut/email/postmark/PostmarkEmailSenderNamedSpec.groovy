package io.micronaut.email.postmark

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.email.EmailSender
import io.micronaut.email.TransactionalEmailSender
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "postmark.api-token", value = "xxx")
@MicronautTest(startApplication = false)
class PostmarkEmailSenderNamedSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "PostmarkEmailSender is annotated with @Named"() {
        expect:
        beanContext.containsBean(PostmarkConfiguration)

        beanContext.containsBean(PostmarkEmailComposer)

        beanContext.containsBean(PostmarkEmailSender)

        beanContext.containsBean(TransactionalEmailSender)

        beanContext.containsBean(PostmarkEmailSender, Qualifiers.byName("postmark"))

        beanContext.containsBean(TransactionalEmailSender, Qualifiers.byName("postmark"))

        and:
        beanContext.containsBean(EmailSender, Qualifiers.byName("postmark"))
    }
}
