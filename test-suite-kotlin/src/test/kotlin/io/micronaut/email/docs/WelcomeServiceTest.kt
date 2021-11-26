package io.micronaut.email.docs

import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertEquals

@MicronautTest(startApplication = false)
class WelcomeServiceTest {
    @Inject
    lateinit var welcomeService: WelcomeService

    @Inject
    lateinit var emailCourier: MockEmailCourier

    @Test
    fun transactionalEmailIsCorrectlyBuilt() {
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
        assertEquals("Hello dear Micronaut user", emailCourier.getEmails()[0].text)
        assertEquals(
            "<html><body><strong>Hello</strong> dear Micronaut user.</body></html>",
            emailCourier.getEmails()[0].html
        )
    }
}