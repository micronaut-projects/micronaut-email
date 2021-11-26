package io.micronaut.email.docs

import io.micronaut.email.EmailSender
import io.micronaut.email.Email
import jakarta.inject.Named
import jakarta.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Named("mock")
@Singleton
class MockEmailSender : EmailSender {
    private val emails: MutableList<Email> = ArrayList()
    override fun send(email: @NotNull @Valid Email) {
        emails.add(email)
    }

    override fun getName(): String = "mock"

    fun getEmails(): List<Email> {
        return this.emails
    }
}