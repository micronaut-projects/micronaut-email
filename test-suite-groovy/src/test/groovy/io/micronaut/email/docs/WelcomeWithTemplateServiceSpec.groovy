package io.micronaut.email.docs

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class WelcomeWithTemplateServiceSpec extends Specification {

    @Inject
    WelcomeWithTemplateService welcomeWithTemplateService

    @Inject
    MockEmailCourier emailCourier

    void "TransactionalEmail is correctly built with templates"() {
        given:
        String message = "Hello dear Micronaut user"
        String copyright =  "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED"
        String address = "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141"

        when:
        welcomeWithTemplateService.sendWelcomeEmail()
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
        emailCourier.emails[0].text.contains(message)
        emailCourier.emails[0].text.contains(copyright)
        emailCourier.emails[0].text.contains(address)
        emailCourier.emails[0].html.contains('<h2 class="cit">' + message + '</h2>')
        emailCourier.emails[0].html.contains('<div>' + copyright + '</div>')
        emailCourier.emails[0].html.contains('<div>' + address + '</div>')
    }
}
