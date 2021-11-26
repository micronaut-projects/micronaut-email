package io.micronaut.email.docs

import io.micronaut.email.EmailCourier
import io.micronaut.email.TransactionalEmail
import jakarta.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Singleton
class MockEmailCourier : EmailCourier {
    private val emails: MutableList<TransactionalEmail> = ArrayList()
    override fun send(email: @NotNull @Valid TransactionalEmail) {
        emails.add(email)
    }

    fun getEmails(): List<TransactionalEmail> {
        return emails
    }
}