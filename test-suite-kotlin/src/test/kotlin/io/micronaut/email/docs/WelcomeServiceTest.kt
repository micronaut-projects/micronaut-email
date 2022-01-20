package io.micronaut.email.docs

import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals

@MicronautTest(startApplication = false)
class WelcomeServiceTest {
    @Inject
    lateinit var welcomeService: WelcomeService

    @Inject
    lateinit var emailSender: MockEmailSender<Any, Any>

    @Test
    fun transactionalEmailIsCorrectlyBuilt() {
        //when:
        welcomeService.sendWelcomeEmail()
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
        assertEquals("Micronaut test", emailSender.getEmails()[0].subject)
        assertEquals("Hello dear Micronaut user", emailSender.getEmails()[0].text)
        assertEquals(
            "<html><body><strong>Hello</strong> dear Micronaut user.</body></html>",
            emailSender.getEmails()[0].html
        )
    }
}