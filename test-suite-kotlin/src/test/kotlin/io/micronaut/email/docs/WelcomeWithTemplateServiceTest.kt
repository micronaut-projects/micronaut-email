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
    lateinit var emailCourier: MockEmailCourier

    @Test
    fun transactionalEmailIsCorrectlyBuilt() {
        //given
        val message = "Hello dear Micronaut user"
        val copyright = "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED"
        val address = "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141"
        //when:
        welcomeService.sendWelcomeEmail()
        //then:
        assertEquals(1, emailCourier.getEmails().size)
        assertEquals(
            "sender@example.com",
            emailCourier.getEmails()[0].sender.from.email
        )
        assertNull(emailCourier.getEmails()[0].sender.from.name)
        assertEquals(1, emailCourier.getEmails()[0].to.size)
        assertEquals("john@example.com", emailCourier.getEmails()[0].to[0].email)
        assertNull(emailCourier.getEmails()[0].to[0].name)
        assertNull(emailCourier.getEmails()[0].cc)
        assertNull(emailCourier.getEmails()[0].bcc)
        assertEquals("Micronaut test", emailCourier.getEmails()[0].subject)
        assertNotNull(emailCourier.getEmails()[0].text)
        assertTrue(emailCourier.getEmails()[0].text!!.contains(message))
        assertTrue(emailCourier.getEmails()[0].text!!.contains(copyright))
        assertTrue(emailCourier.getEmails()[0].text!!.contains(address))
        assertNotNull(emailCourier.getEmails()[0].html)
        assertTrue(emailCourier.getEmails()[0].html!!.contains("<h2 class=\"cit\">$message</h2>"))
        assertTrue(emailCourier.getEmails()[0].html!!.contains("<div>$copyright</div>"))
        assertTrue(emailCourier.getEmails()[0].html!!.contains("<div>$address</div>"))
    }
}