package io.micronaut.email.docs

import io.micronaut.core.annotation.NonNull
import io.micronaut.email.EmailCourier
import io.micronaut.email.TransactionalEmail
import jakarta.inject.Singleton

import javax.validation.Valid
import javax.validation.constraints.NotNull

@Singleton
class MockEmailCourier implements EmailCourier {
    List<TransactionalEmail> emails = []
    @Override
    void send(@NonNull @NotNull @Valid TransactionalEmail email) {
        emails << email
    }
}
