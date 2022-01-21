package io.micronaut.email

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.email.mailjet.MailjetConfiguration
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "mailjet.enabled", value = StringUtils.FALSE)
@Property(name = "mailjet.api-secret", value = "xxx")
@Property(name = "mailjet.api-key", value = "yyyy")
@MicronautTest(startApplication = false)
class MailjetToggeableSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "if you set mailjet.enabled to false you disable Mailjet integration"() {
        expect:
        !beanContext.containsBean(MailjetConfiguration)
    }
}
