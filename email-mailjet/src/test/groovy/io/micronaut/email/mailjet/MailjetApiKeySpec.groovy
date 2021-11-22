package io.micronaut.email.mailjet

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "mailjet.api-key", value = "xxx")
@Property(name = "mailjet.api-secret", value = "yyy")
@MicronautTest(startApplication = false)
class MailjetApiKeySpec extends Specification {

    @Inject
    BeanContext beanContext

    void "if you set mailjet.api-token and mailjet.api-secret MailjetConfiguration is present"() {
        expect:
        beanContext.containsBean(MailjetConfiguration)
        'xxx' == beanContext.getBean(MailjetConfiguration).apiKey
        'yyy' == beanContext.getBean(MailjetConfiguration).apiSecret
    }
}
