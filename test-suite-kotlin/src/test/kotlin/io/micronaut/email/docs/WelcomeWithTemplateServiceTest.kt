package io.micronaut.email.docs

import io.micronaut.email.BodyType
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
    lateinit var emailSender: MockEmailSender<Any, Any>

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
        assertNotNull(emailSender.getEmails()[0].body)

        assertNotNull(emailSender.getEmails()[0].body)
        assertTrue(emailSender.getEmails()[0].body!!.get(BodyType.TEXT).isPresent)
        val text = emailSender.getEmails()[0].body!!.get(BodyType.TEXT).get()
        assertTrue(text.contains(message))
        assertTrue(text.contains(copyright))
        assertTrue(text.contains(address))
        assertTrue(emailSender.getEmails()[0].body!!.get(BodyType.HTML).isPresent)
        val html = emailSender.getEmails()[0].body!!.get(BodyType.HTML).get()
        assertTrue(html.contains("<h2 class=\"cit\">$message</h2>"))
        assertTrue(html.contains("<div>$copyright</div>"))
        assertTrue(html.contains("<div>$address</div>"))
    }
}
