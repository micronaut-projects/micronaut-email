package io.micronaut.email.postmark

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "postmark.api-token", value = "xxx")
@MicronautTest(startApplication = false)
class PostmarkApiKeySpec extends Specification {

    @Inject
    BeanContext beanContext

    void "if you set postmark.enabled to false you disable Postmark integration"() {
        expect:
        beanContext.containsBean(PostmarkConfiguration)
        'xxx' == beanContext.getBean(PostmarkConfiguration).apiToken
    }
}
