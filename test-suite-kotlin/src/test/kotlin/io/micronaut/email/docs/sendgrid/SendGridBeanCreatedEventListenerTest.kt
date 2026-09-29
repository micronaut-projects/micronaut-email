package io.micronaut.email.docs.sendgrid

import com.sendgrid.SendGrid
import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Property(name = "spec.name", value = "SendGridBeanCreatedEventListenerTest")
@Property(name = "sendgrid.api-key", value = "xxx")
@Property(name = "javamail.enabled", value = StringUtils.FALSE)
@MicronautTest(startApplication = false)
class SendGridBeanCreatedEventListenerTest {

    @Inject
    lateinit var sendGrid: SendGrid

    @Test
    fun sendGridBeanIsCustomizedByTheListener() {
        assertEquals(5000, sendGrid.rateLimitSleep)
    }
}
