package io.micronaut.email

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Singleton
import spock.lang.Specification

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import java.util.function.Consumer

@Property(name = 'spec.name', value = 'FromDecoratorSpec')
@Property(name = 'micronaut.email.from.email', value = 'tcook@apple.com')
@MicronautTest(startApplication = false)
class FromDecoratorSpec extends Specification {

    @Inject
    EmailSender<?, ?> emailSender

    @Inject
    BeanContext beanContext

    void "FromDecorator decorates email"() {
        when:
        emailSender.send(Email.builder()
                .to("ecue@apple.com")
                .subject("Hello World")
                .body("I love Apple Music"))

        then:
        beanContext.getBean(MockEmailSender).getEmails()
        new Contact("tcook@apple.com") == beanContext.getBean(MockEmailSender).getEmails().get(0).from
    }

    @Requires(property = 'spec.name', value = 'FromDecoratorSpec')
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
