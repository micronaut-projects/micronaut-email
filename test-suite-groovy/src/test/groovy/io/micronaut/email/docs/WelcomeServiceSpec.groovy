package io.micronaut.email.docs

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class WelcomeServiceSpec extends Specification {

    @Inject
    WelcomeService welcomeService

    @Inject
    MockEmailSender emailSender

    void "Email is correctly built"() {
        when:
        welcomeService.sendWelcomeEmail()
        then:
        emailSender.emails.size()
        'sender@example.com' == emailSender.emails[0].from.email
        !emailSender.emails[0].from.name
        1 == emailSender.emails[0].to.size()
        'john@example.com' == emailSender.emails[0].to[0].email
        !emailSender.emails[0].to[0].name
        !emailSender.emails[0].cc
        !emailSender.emails[0].bcc
        "Micronaut test" == emailSender.emails[0].subject
        "Hello dear Micronaut user" == emailSender.emails[0].text
        "<html><body><strong>Hello</strong> dear Micronaut user.</body></html>" == emailSender.emails[0].html
    }
}
