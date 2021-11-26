package io.micronaut.email

import io.micronaut.core.beans.BeanIntrospection
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.PendingFeature
import spock.lang.Specification

import javax.validation.Validator

@MicronautTest(startApplication = false)
class EmailHeaderSpec extends Specification {

    @Inject
    Validator validator

    void "EmailHeader is annotated with @Introspected"() {
        when:
        BeanIntrospection.getIntrospection(EmailHeader)

        then:
        noExceptionThrown()
    }

    void "from, to and subject are required validation"() {
        given:
        EmailHeader email = EmailHeader.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .subject("Apple Music")
                .build()

        expect:
        !validator.validate(email)
    }

    void "from is required"() {
        when:
        EmailHeader.builder()
                .to("ecue@apple.com")
                .subject("Apple Music")
                .build()

        then:
        thrown(IllegalArgumentException)
    }

    void "from must be a valid email address"() {
        given:
        EmailHeader email = EmailHeader.builder()
                .from("tim")
                .to("ecue@apple.com")
                .subject("Apple Music")
                .build()

        expect:
        validator.validate(email)
    }

    void "to must be a valid email address"() {
        given:
        EmailHeader email = EmailHeader.builder()
                .from("tcook@apple.com")
                .to("ecue")
                .subject("Apple Music")
                .build()

        expect:
        validator.validate(email)
    }

    void "cc must be a valid email address"() {
        given:
        EmailHeader email = EmailHeader.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .cc("lmaestri")
                .subject("Apple Music")
                .build()

        expect:
        validator.validate(email)
    }

    void "bcc must be a valid email address"() {
        given:
        EmailHeader email = EmailHeader.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .bcc("lmaestri")
                .subject("Apple Music")
                .build()

        expect:
        validator.validate(email)
    }

    void "to is required"() {
        when:
        EmailHeader.builder()
                .from("tcook@apple.com")
                .subject("Apple Music")
                .build()
        then:
        thrown(IllegalArgumentException)
    }

    void "subject is required"() {
        when:
        EmailHeader.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .build()
        then:
        thrown(IllegalArgumentException)
    }
}
