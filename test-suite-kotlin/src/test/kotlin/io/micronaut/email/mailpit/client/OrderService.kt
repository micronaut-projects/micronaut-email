package io.micronaut.email.mailpit.client

import io.micronaut.context.annotation.Requires
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import jakarta.inject.Singleton
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Email as EmailConstraint

@Requires(property = "spec.name", value = "OrderServiceTest")
@Singleton
class OrderService(private val emailSender: EmailSender<*, *>) {

    fun sendOrderEmail(
        @EmailConstraint recipient: String,
        @NotBlank orderNumber: String
    ) {
        val text = "We have received your order $orderNumber. You will receive your product soon."
        val html = "<html><body><p>$text</p></body></html>"
        emailSender.send(
            Email.builder()
                .to(recipient)
                .subject("Order Number: $orderNumber")
                .body(html, text)
        )
    }
}
