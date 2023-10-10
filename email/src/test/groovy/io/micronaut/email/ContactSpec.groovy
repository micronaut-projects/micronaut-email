package io.micronaut.email

import io.micronaut.core.beans.BeanIntrospection
import spock.lang.Specification
import spock.lang.Unroll

class ContactSpec extends Specification {
    void "Contact is annotated with @Introspected"() {
        when:
        BeanIntrospection.getIntrospection(Contact)

        then:
        noExceptionThrown()
    }

    @Unroll
    void "Contact::getNameAddress"(String email, String name, String expected) {
        when:
        new Contact(email, name).getNameAddress()


        then:
        noExceptionThrown()

        where:
        email                  | name        | expected
        'johnsnow@example.com' | 'John Snow' | 'John Snow <johnsnow@example.com>'
        'johnsnow@example.com' | null        | '<johnsnow@example.com>'
    }
}
