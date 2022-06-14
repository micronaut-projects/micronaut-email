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
class MockEmailSender<I, O> : TransactionalEmailSender<I, Email> {

    private val emails: MutableList<Email> = mutableListOf()
    private val requests: MutableList<Consumer<I>> = mutableListOf()

    fun getEmails() = emails
    fun getRequests() = requests

    @NonNull
    override fun getName(): String {
        return "mock"
    }

    @Throws(EmailException::class)
    override fun send(email: Email, emailRequest: Consumer<I>): Email {
        emails.add(email)
        requests.add(emailRequest)
        return email
    }
}