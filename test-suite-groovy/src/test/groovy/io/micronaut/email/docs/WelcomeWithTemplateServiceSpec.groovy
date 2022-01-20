package io.micronaut.email.docs

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class WelcomeWithTemplateServiceSpec extends Specification {

    @Inject
    WelcomeWithTemplateService welcomeWithTemplateService

    @Inject
    MockEmailSender emailSender

    void "Email is correctly built with templates"() {
        given:
        String message = "Hello dear Micronaut user"
        String copyright =  "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED"
        String address = "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141"

        when:
        welcomeWithTemplateService.sendWelcomeEmail()
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
        emailSender.emails[0].body
        emailSender.emails[0].body.get().contains('<h2 class="cit">' + message + '</h2>')
        emailSender.emails[0].body.get().contains('<div>' + copyright + '</div>')
        emailSender.emails[0].body.get().contains('<div>' + address + '</div>')
    }
}
