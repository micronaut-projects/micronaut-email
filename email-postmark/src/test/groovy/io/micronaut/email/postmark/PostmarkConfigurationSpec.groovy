package io.micronaut.email.postmark

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Requires
import spock.lang.Specification

@MicronautTest(startApplication = false)
class PostmarkConfigurationSpec extends Specification {

    @Inject
    BeanContext beanContext

    @IgnoreIf({env["POSTMARK_API_TOKEN"]})
    void "by default there is no bean of type PostmarkConfiguration"() {
        expect:
        !beanContext.containsBean(PostmarkConfiguration)
    }
}
