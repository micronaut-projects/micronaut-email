package io.micronaut.email.docs

import io.micronaut.email.Email
import io.micronaut.email.TransactionalEmailSender
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.util.Optional
import kotlin.collections.ArrayList

@Named("mock")
@Singleton
class MockEmailSender : TransactionalEmailSender<Void> {
    private val emails: MutableList<Email> = ArrayList()

    override fun send(email: Email): Optional<Void> {
        emails.add(email)
        return Optional.empty()
    }

    override fun getName(): String = "mock"

    fun getEmails(): List<Email> {
        return this.emails
    }
}