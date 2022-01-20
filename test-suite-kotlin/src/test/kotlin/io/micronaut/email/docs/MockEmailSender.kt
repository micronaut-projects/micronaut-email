package io.micronaut.email.docs

import io.micronaut.core.annotation.NonNull
import io.micronaut.email.Email
import io.micronaut.email.EmailException
import io.micronaut.email.TransactionalEmailSender
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.util.function.Consumer

@Named("mock")
@Singleton
class MockEmailSender<I, O> : TransactionalEmailSender<I, O?> {
    private val emails: MutableList<Email> = ArrayList()
    fun getEmails(): List<Email> {
        return emails
    }

    @NonNull
    override fun getName(): String {
        return "mock"
    }

    @Throws(EmailException::class)
    override fun send(email: Email, emailRequest: Consumer<I>): O? {
        emails.add(email)
        return null
    }
}