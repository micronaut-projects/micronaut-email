package io.micronaut.email.docs

import io.micronaut.core.annotation.NonNull
import io.micronaut.email.Email
import io.micronaut.email.EmailException
import io.micronaut.email.TransactionalEmailSender
import jakarta.inject.Named
import jakarta.inject.Singleton

import javax.validation.Valid
import javax.validation.constraints.NotNull
import java.util.function.Consumer

@Named("mock")
@Singleton
class MockEmailSender<I> implements TransactionalEmailSender<I, Email> {
    List<Email> emails = []
    List<Consumer<I>> requests = []

    @Override
    @NonNull
    String getName() {
        "mock"
    }

    @Override
    @NonNull
    Email send(@NonNull @NotNull @Valid Email email,
           @NonNull @NotNull Consumer<I> emailRequest) throws EmailException {
        emails.add(email)
        requests.add(emailRequest);
        email
    }
}
