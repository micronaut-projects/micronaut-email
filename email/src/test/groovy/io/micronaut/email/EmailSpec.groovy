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

    void "trackLinksInHtml sets TrackLinks.HTML"() {
        given:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .subject("Apple Music")
                .text("I love Apple Music")
                .trackLinksInHtml()
                .build()

        expect:
        !validator.validate(email)
        email.trackLinks == TrackLinks.HTML
    }

    void "trackLinksInHtml sets TrackLinks.TEXT"() {
        given:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .subject("Apple Music")
                .text("I love Apple Music")
                .trackLinksInText()
                .build()

        expect:
        !validator.validate(email)
        email.trackLinks == TrackLinks.TEXT
    }

    void "trackLinksInHtml sets TrackLinks.HTML_AND_TEXT"() {
        given:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .subject("Apple Music")
                .text("I love Apple Music")
                .trackLinksInHtmlAndText()
                .build()

        expect:
        !validator.validate(email)
        email.trackLinks == TrackLinks.HTML_AND_TEXT
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
        Email email = Email.builder()
                .to("ecue@apple.com")
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()

        then:
        validator.validate(email)
    }

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

    void "At least a recipient (to, cc or bcc) is required"() {
        when:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()
        then:
        validator.validate(email)
    }

    void "to is not required if you provide a cc"() {
        when:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .cc('ecue@apple.com')
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()
        then:
        !validator.validate(email)
    }

    void "to is not required if you provide a bcc"() {
        when:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .bcc('ecue@apple.com')
                .subject("Apple Music")
                .text("Stream music to your device")
                .build()
        then:
        !validator.validate(email)
    }

    void "subject is required"() {
        when:
        Email email = Email.builder()
                .from("tcook@apple.com")
                .to("ecue@apple.com")
                .text("Stream music to your device")
                .build()
        then:
        validator.validate(email)
    }
}
