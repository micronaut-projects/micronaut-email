package io.micronaut.email.docs

import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.MultipartBody
import jakarta.inject.Singleton
import jakarta.mail.Message
import jakarta.mail.MessagingException
import org.slf4j.LoggerFactory

/**
 * An example of customization for JavaMail messages
 */
@Singleton
class CustomizedJavaMailService(private val emailSender: EmailSender<Message, *>) {

    fun sendCustomizedEmail() {
        val email = Email.builder()
            .from("sender@example.com")
            .to("john@example.com")
            .subject("Micronaut test")
            .body(
                MultipartBody(
                    "<html><body><strong>Hello</strong> dear Micronaut user.</body></html>",
                    "Hello dear Micronaut user"
                )
            )

        // Customize the message with a header prior to sending
        emailSender.send(email) { message: Message ->
            try {
                message.addHeader("List-Unsubscribe", "<mailto:list@host.com?subject=unsubscribe>")
            } catch (e: MessagingException) {
                LOG.error("Failed to add header", e)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CustomizedJavaMailService::class.java)
    }
}
