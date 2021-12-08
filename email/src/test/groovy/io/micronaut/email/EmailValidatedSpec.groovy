package io.micronaut.email

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.email.validation.EmailMessages
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Singleton
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Property(name = 'spec.name', value = 'EmailValidatedSpec')
@MicronautTest(startApplication = false)
class EmailValidatedSpec extends Specification {

    @Inject
    EmailSender<Void> emailSender

    @Inject
    BeanContext beanContext

    @Unroll("#description")
    void "Email is validated before is sent"(List<String> messages, Email email, String description) {
        when: 'without a from address'
        emailSender.send(email)

        then: 'a validation exception is thrown'
        ConstraintViolationException e = thrown()
        messages == e.constraintViolations*.message

        where:
        messages                               | email                                                                                              | description
        [EmailMessages.ANY_RECIPIENT_MESSAGE]  | Email.builder().from("tcook@apple.com").subject("Hello World").text("I love Apple Music").build()  | 'any recipient is validated'
        ["must not be blank"]                  | Email.builder().from("tcook@apple.com").to("ecue@apple.com").text("I love Apple Music").build()    | 'subject is validated'
        ["must not be null"]                   | Email.builder().subject("Hello World").to("ecue@apple.com").text("I love Apple Music").build()     | 'from is validated'
        [EmailMessages.ANY_CONTENT_MESSAGE]    | Email.builder().from("tcook@apple.com").to("ecue@apple.com").subject("Hello World").build()        | 'text or html is required'
    }

    @Requires(property = 'spec.name', value = 'EmailValidatedSpec')
    @Named("mock")
    @Singleton
    static class MockEmailSender implements TransactionalEmailSender<Void> {
        List<Email> emails = []

        @Override
        Optional<Void> send(@NonNull @NotNull @Valid Email email) {
            emails << email
            return null
        }

        @Override
        String getName() {
            'mock'
        }
    }

}
