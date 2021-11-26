package io.micronaut.email.docs

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class WelcomeServiceSpec extends Specification {

    @Inject
    WelcomeService welcomeService

    @Inject
    MockEmailCourier emailCourier

    void "TransactionalEmail is correctly built"() {
        when:
        welcomeService.sendWelcomeEmail()
        then:
        emailCourier.emails.size()
        'sender@example.com' == emailCourier.emails[0].sender.from.email
        !emailCourier.emails[0].sender.from.name
        1 == emailCourier.emails[0].to.size()
        'john@example.com' == emailCourier.emails[0].to[0].email
        !emailCourier.emails[0].to[0].name
        !emailCourier.emails[0].cc
        !emailCourier.emails[0].bcc
        "Micronaut test" == emailCourier.emails[0].subject
        "Hello dear Micronaut user" == emailCourier.emails[0].text
        "<html><body><strong>Hello</strong> dear Micronaut user.</body></html>" == emailCourier.emails[0].html
    }
}
