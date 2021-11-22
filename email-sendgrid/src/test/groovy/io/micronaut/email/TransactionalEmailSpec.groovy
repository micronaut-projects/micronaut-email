package io.micronaut.email

import io.micronaut.core.beans.BeanIntrospection
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.PendingFeature
import spock.lang.Specification
import javax.validation.Validator

@MicronautTest(startApplication = false)
class TransactionalEmailSpec extends Specification {

    @Inject
    Validator validator

    void "TransactionalEmail is annotated with @Introspected"() {
        when:
        BeanIntrospection.getIntrospection(TransactionalEmail)

        then:
        noExceptionThrown()
    }

    void "from, to and subject are required validation"() {
        given:
        TransactionalEmail email = TransactionalEmail.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .subject("Apple Music")
                .build()

        expect:
        !validator.validate(email)
    }

    void "from is required"() {
        given:
        TransactionalEmail email = new TransactionalEmail(null, [new Contact("ecue@apple.com")], "Apple Music")

        expect:
        validator.validate(email)
    }

    void "from must be a valid email address"() {
        given:
        TransactionalEmail email = new TransactionalEmail(new Contact("tim"), [new Contact("ecue@apple.com")], "Apple Music")

        expect:
        validator.validate(email)
    }

    void "to must be a valid email address"() {
        given:
        TransactionalEmail email = new TransactionalEmail(new Contact("tcook@apple.com"), [new Contact("ecue")], "Apple Music")

        expect:
        validator.validate(email)
    }

    void "cc must be a valid email address"() {
        given:
        TransactionalEmail email = new TransactionalEmail(new Contact("tcook@apple.com"), [new Contact("ecue@apple.com")], "Apple Music",
                null,
                [new Contact("lmaestri")],
                null,
                null,
                null)
        expect:
        validator.validate(email)
    }

    void "bcc must be a valid email address"() {
        given:
        TransactionalEmail email = new TransactionalEmail(new Contact("tcook@apple.com"), [new Contact("ecue@apple.com")], "Apple Music",
                null,
                null,
                [new Contact("lmaestri")],
                null,
                null)

        expect:
        validator.validate(email)
    }

    void "to is required"() {
        given:
        TransactionalEmail email = new TransactionalEmail(new Contact("tcooke@apple.com"), null, "Apple Music")

        expect:
        validator.validate(email)
    }

    void "subject is required"() {
        given:
        TransactionalEmail email = new TransactionalEmail(new Contact("tcooke@apple.com"), [new Contact("ecue@apple.com")], null)

        expect:
        validator.validate(email)
    }
}
