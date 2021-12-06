package io.micronaut.email.docs

import io.micronaut.email.EmailSender
import io.micronaut.email.Email
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull
import kotlin.collections.ArrayList

@Named("mock")
@Singleton
class MockEmailSender : EmailSender<Void> {
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