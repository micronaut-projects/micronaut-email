package io.micronaut.email.mailpit.client

import io.micronaut.context.annotation.Property
import io.micronaut.email.configuration.FromConfiguration
import io.micronaut.email.mailpit.client.model.MailpitAddress
import io.micronaut.email.mailpit.client.model.MailpitMessage
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.util.UUID

@Property(name = "spec.name", value = "OrderServiceTest")
@Property(name = "micronaut.email.from.email", value = "info@micronaut.io")
@MicronautTest(startApplication = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class OrderServiceTest : TestPropertyProvider {
    override fun getProperties(): Map<String, String> {
        return Mailpit.getProperties()
    }

    @AfterAll
    fun cleanupSpec() {
        Mailpit.close()
    }

    @Test
    fun orderService(
        orderService: OrderService,
        client: MailpitClient,
        fromConfiguration: FromConfiguration
    ) {
        val recipient = "example@micronaut.io"
        val orderNumber = UUID.randomUUID().toString()
        val text = "We have received your order $orderNumber. You will receive your product soon."
        val html = "<html><body><p>$text</p></body></html>"

        orderService.sendOrderEmail(recipient, orderNumber)

        val message: MailpitMessage = client.getMessage("latest")

        assertNotNull(message)
        assertNotNull(message.from())
        assertNotNull(message.to())
        val from: MailpitAddress = message.from()!!
        val to: List<MailpitAddress> = message.to()!!
        assertEquals(fromConfiguration.from.email, from.address())
        assertEquals(listOf(recipient), to.map { it.address() })
        assertEquals("Order Number: $orderNumber", message.subject())
        assertEquals(text, message.text())
        assertEquals(html, message.html())
    }

    private object Mailpit {
        private val container: GenericContainer<*> = MailpitContainer()
            .withExposedPorts(1025, 8025)
            .waitingFor(Wait.forHttp("/").forPort(8025))

        fun getProperties(): Map<String, String> {
            return getProperties(getRunningContainer())
        }

        private fun getRunningContainer(): GenericContainer<*> {
            do {
                container.start()
            } while (!container.isRunning)
            return container
        }

        private fun getProperties(container: GenericContainer<*>): Map<String, String> {
            return mapOf(
                "javamail.properties.mail.smtp.host" to container.host,
                "javamail.properties.mail.smtp.port" to "${container.getMappedPort(1025)}",
                "micronaut.http.services.mailpit.url" to "http://${container.host}:${container.getMappedPort(8025)}"
            )
        }

        fun close() {
            container.close()
        }
    }

    private class MailpitContainer : GenericContainer<MailpitContainer>(DockerImageName.parse("axllent/mailpit"))
}
