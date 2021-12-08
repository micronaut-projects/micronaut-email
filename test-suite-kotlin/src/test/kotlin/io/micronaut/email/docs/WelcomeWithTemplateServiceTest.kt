package io.micronaut.email.docs

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
class WelcomeWithTemplateServiceTest {
    @Inject
    lateinit var welcomeService: WelcomeWithTemplateService

    @Inject
    lateinit var emailSender: MockEmailSender

    @Test
    fun transactionalEmailIsCorrectlyBuilt() {
        //given
        val message = "Hello dear Micronaut user"
        val copyright = "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED"
        val address = "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141"
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
        assertNotNull(emailSender.getEmails()[0].text)
        assertTrue(emailSender.getEmails()[0].text!!.contains(message))
        assertTrue(emailSender.getEmails()[0].text!!.contains(copyright))
        assertTrue(emailSender.getEmails()[0].text!!.contains(address))
        assertNotNull(emailSender.getEmails()[0].html)
        assertTrue(emailSender.getEmails()[0].html!!.contains("<h2 class=\"cit\">$message</h2>"))
        assertTrue(emailSender.getEmails()[0].html!!.contains("<div>$copyright</div>"))
        assertTrue(emailSender.getEmails()[0].html!!.contains("<div>$address</div>"))
    }
}