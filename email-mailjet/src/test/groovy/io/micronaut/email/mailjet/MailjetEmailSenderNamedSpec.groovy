package io.micronaut.email.mailjet

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.email.EmailSender
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "mailjet.api-secret", value = "xxx")
@Property(name = "mailjet.api-key", value = "yyyy")
@MicronautTest(startApplication = false)
class MailjetEmailSenderNamedSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "MailjetEmailSender is annotated with @Named"() {
        expect:
        beanContext.containsBean(EmailSender, Qualifiers.byName("mailjet"))
    }
}
