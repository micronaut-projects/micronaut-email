package io.micronaut.email

import io.micronaut.core.beans.BeanIntrospection
import spock.lang.Specification

class ContactSpec extends Specification {
    void "Contact is annotated with @Introspected"() {
        when:
        BeanIntrospection.getIntrospection(Contact)

        then:
        noExceptionThrown()
    }
}
