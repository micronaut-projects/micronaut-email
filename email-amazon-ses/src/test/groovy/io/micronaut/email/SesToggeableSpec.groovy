package io.micronaut.email

import io.micronaut.email.ses.SesConfiguration
import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "ses.enabled", value = StringUtils.FALSE)
@MicronautTest(startApplication = false)
class SesToggeableSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "if you set ses.enabled to false you disable Ses integration"() {
        expect:
        !beanContext.containsBean(SesConfiguration)
    }
}
