package io.micronaut.email

import io.micronaut.core.beans.BeanIntrospection
import spock.lang.Specification

class RecipientSpec extends Specification {
    void "Recipient is annotated with @Introspected"() {
        when:
        BeanIntrospection.getIntrospection(Recipient)

        then:
        noExceptionThrown()
    }
}
