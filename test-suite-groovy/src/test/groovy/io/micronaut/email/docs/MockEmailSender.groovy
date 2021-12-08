package io.micronaut.email.docs

import io.micronaut.core.annotation.NonNull
import io.micronaut.email.Email
import io.micronaut.email.TransactionalEmailSender
import jakarta.inject.Named
import jakarta.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Named("mock")
@Singleton
class MockEmailSender implements TransactionalEmailSender<Void> {
    List<Email> emails = []
    @Override
    @NonNull
    String getName() {
        return "mock"
    }

    @Override
    @NonNull
    Optional<Void> send(@NonNull @NotNull @Valid Email email) {
        emails << email
        Optional.empty()
    }
}
