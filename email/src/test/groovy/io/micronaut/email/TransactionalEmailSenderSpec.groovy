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

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import java.util.function.Consumer

@Property(name = 'spec.name', value = 'TransactionalEmailSenderSpec')
@MicronautTest(startApplication = false)
class TransactionalEmailSenderSpec extends Specification {

    @Inject
    TransactionalEmailSender<?, ?> emailSender

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
        [EmailMessages.ANY_RECIPIENT_MESSAGE]  | Email.builder().from("tcook@apple.com").subject("Hello World").body("I love Apple Music").build()  | 'any recipient is validated'
        ["must not be blank"]                  | Email.builder().from("tcook@apple.com").to("ecue@apple.com").body("I love Apple Music").build()    | 'subject is validated'
        ["must not be null"]                   | Email.builder().subject("Hello World").to("ecue@apple.com").body("I love Apple Music").build()     | 'from is validated'
        ["must not be null"]                   | Email.builder().from("tcook@apple.com").to("ecue@apple.com").subject("Hello World").build()        | 'body is required'
    }

    @Requires(property = 'spec.name', value = 'TransactionalEmailSenderSpec')
    @Named("mock")
    @Singleton
    static class MockEmailSender<I, O> implements TransactionalEmailSender<I, O> {
        List<Email> emails = []

        @Override
        @NonNull
        O send(@NonNull @NotNull @Valid Email email,
               @NonNull @NotNull Consumer<I> emailRequest) throws EmailException {
            emails << email
            email
        }

        @Override
        String getName() {
            'mock'
        }
    }

}
