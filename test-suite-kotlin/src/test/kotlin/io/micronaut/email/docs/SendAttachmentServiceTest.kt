package io.micronaut.email.docs

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
class SendAttachmentServiceTest {
    @Inject
    lateinit var sendAttachmentService: SendAttachmentService

    @Inject
    lateinit var emailSender: MockEmailSender<Any, Any>

    @Test
    fun transactionalEmailIsCorrectlyBuilt() {
        //when:
        sendAttachmentService.sendWelcomeEmail()
        //then:
        assertEquals(1, emailSender.getEmails().size)
        assertEquals(
            "sender@example.com",
            emailSender.getEmails()[0].from.email
        )
        assertNull(emailSender.getEmails()[0].from.name)
        assertNotNull(emailSender.getEmails()[0].to)
        assertEquals(1, emailSender.getEmails()[0].to!!.size)
        assertEquals("john@example.com", emailSender.getEmails()[0].to!!.first().email)
        assertNull(emailSender.getEmails()[0].to!!.first().name)
        assertNull(emailSender.getEmails()[0].cc)
        assertNull(emailSender.getEmails()[0].bcc)
        assertEquals("Monthly reports", emailSender.getEmails()[0].subject)
        assertEquals(
            "<html><body><strong>Attached Monthly reports</strong>.</body></html>",
            emailSender.getEmails()[0].body!!.get()
        )
        assertNotNull(emailSender.getEmails()[0].attachments)
        assertEquals(1, emailSender.getEmails()[0].attachments!!.size)
        assertEquals("reports.xlsx", emailSender.getEmails()[0].attachments!![0].filename)
        assertEquals(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            emailSender.getEmails()[0].attachments!![0].contentType
        )
        assertNotNull(emailSender.getEmails()[0].attachments!![0].content)
    }
}
