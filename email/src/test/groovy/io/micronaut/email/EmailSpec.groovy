package io.micronaut.email

import io.micronaut.core.beans.BeanIntrospection
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.PendingFeature
import spock.lang.Specification
import javax.validation.Validator

@MicronautTest(startApplication = false)
class EmailSpec extends Specification {

    @Inject
    Validator validator

    void "Email is annotated with @Introspected"() {
        when:
        BeanIntrospection.getIntrospection(Email)

        then:
        noExceptionThrown()
    }

    void "from, to and subject are required validation"() {
        given:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()
        expect:
        !validator.validate(email)
    }

    void "trackOpens accepts a boolean"() {
        given:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .subject("Apple Music")
                .text("Stream music to your device")
                .trackOpens(true)
                .build()

        expect:
        !validator.validate(email)
        email.trackOpens
    }

    void "trackLinks accepts an enum"() {
        given:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .subject("Apple Music")
                .text("Stream music to your device")
                .trackLinks(TrackLinks.HTML)
                .build()

        expect:
        !validator.validate(email)
        email.trackLinks == TrackLinks.HTML
    }

    void "from is required"() {
        when:
        Email.builder()
                .to("ecue@apple.com")
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()

        then:
        thrown(IllegalArgumentException)
    }

    @PendingFeature
    void "from must be a valid email address"() {
        given:
        Email email = Email.builder()
                .from("tim")
                .to("ecue@apple.com")
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()

        expect:
        validator.validate(email)
    }

    @PendingFeature
    void "to must be a valid email address"() {
        given:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .to("ecue")
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()

        expect:
        validator.validate(email)
    }

    @PendingFeature
    void "cc must be a valid email address"() {
        given:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .cc("lmaestri")
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()

        expect:
        validator.validate(email)
    }

    @PendingFeature
    void "bcc must be a valid email address"() {
        given:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .bcc("lmaestri")
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()

        expect:
        validator.validate(email)
    }

    void "to is required"() {
        when:
        Email.builder()
                .from("tcook@apple.com")
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()
        then:
        thrown(IllegalArgumentException)
    }

    void "subject is required"() {
        when:
        Email.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .text("Stream music to your device")
                .build()
        then:
        thrown(IllegalArgumentException)
    }
}
