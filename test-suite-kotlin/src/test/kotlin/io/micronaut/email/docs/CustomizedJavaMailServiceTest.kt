package io.micronaut.email.docs

import io.micronaut.email.BodyType
import io.micronaut.email.Email
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.function.Consumer
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.MimeMessage

@MicronautTest(startApplication = false)
internal class CustomizedJavaMailServiceTest {
    @Inject
    lateinit var customizedJavaMailService: CustomizedJavaMailService

    @Inject
    lateinit var emailSender: MockEmailSender<Message, Any>

    @Test
    fun transactionalHtmlEmailIsCorrectlyBuilt() {
        //when:
        customizedJavaMailService.sendCustomizedEmail()

        //then:
        assertEquals(1, emailSender.getEmails().size)
        val email: Email = emailSender.getEmails()[0]
        val consumer: Consumer<Message> = emailSender.getRequests()[0]

        assertEquals("sender@example.com", email.from.email)
        Assertions.assertNull(email.from.name)
        assertEquals(1, email.to!!.size)
        assertEquals("john@example.com", email.to!!.stream().findFirst().get().email)
        Assertions.assertNull(email.to!!.stream().findFirst().get().name)
        Assertions.assertNull(email.cc)
        Assertions.assertNull(email.bcc)
        assertEquals("Micronaut test", email.subject)
        Assertions.assertNotNull(email.body)
        Assertions.assertTrue(email.body!![BodyType.TEXT].isPresent)
        assertEquals("Hello dear Micronaut user", email.body!![BodyType.TEXT].get())
        Assertions.assertTrue(email.body!![BodyType.HTML].isPresent)
        assertEquals(
            "<html><body><strong>Hello</strong> dear Micronaut user.</body></html>",
            email.body!![BodyType.HTML].get()
        )

        //when:
        val message = MessageHeaderCapture()
        consumer.accept(message)

        //then:
        assertEquals("List-Unsubscribe", message.name)
        assertEquals("<mailto:list@host.com?subject=unsubscribe>", message.value)
    }

    internal class MessageHeaderCapture : MimeMessage(null as Session?) {
        var name: String? = null
        var value: String? = null

        @Throws(MessagingException::class)
        override fun addHeader(name: String, value: String) {
            this.name = name
            this.value = value
        }
    }
}