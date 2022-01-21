package io.micronaut.email

import io.micronaut.core.beans.BeanIntrospection
import spock.lang.Specification

class AttachmentSpec extends Specification {

    void "Attachment is annotated with @Introspected"() {
        when:
        BeanIntrospection.getIntrospection(Attachment)

        then:
        noExceptionThrown()
    }
}
