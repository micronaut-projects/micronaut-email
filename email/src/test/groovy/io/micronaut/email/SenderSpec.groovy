package io.micronaut.email

import io.micronaut.core.beans.BeanIntrospection
import spock.lang.Specification

class SenderSpec extends Specification {
    void "Sender is annotated with @Introspected"() {
        when:
        BeanIntrospection.getIntrospection(Sender)

        then:
        noExceptionThrown()
    }
}
