package io.micronaut.email.docs

import io.micronaut.email.EmailSender
import io.micronaut.email.Email
import io.micronaut.email.MultipartBody
import jakarta.inject.Singleton

@Singleton
class WelcomeService(private val emailSender: EmailSender<Any, Any>) {
    fun sendWelcomeEmail() {
        emailSender.send(
            Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .body(MultipartBody("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>", "Hello dear Micronaut user"))
        )
    }
}
