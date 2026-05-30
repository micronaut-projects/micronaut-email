package io.micronaut.email.mailpit.client

import io.micronaut.context.annotation.Property
import io.micronaut.email.configuration.FromConfiguration
import io.micronaut.email.mailpit.client.model.MailpitAddress
import io.micronaut.email.mailpit.client.model.MailpitMessage
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

@Property(name = "spec.name", value = "OrderServiceTest")
@Property(name = "micronaut.email.from.email", value = "info@micronaut.io")
@spock.lang.Requires({ org.testcontainers.DockerClientFactory.instance().isDockerAvailable() })
@MicronautTest(startApplication = false)
class OrderServiceTest extends Specification implements TestPropertyProvider {
    @Inject
    OrderService orderService

    @Inject
    MailpitClient client

    @Inject
    FromConfiguration fromConfiguration

    @Override
    Map<String, String> getProperties() {
        Mailpit.getProperties()
    }

    void cleanupSpec() {
        Mailpit.close()
    }

    void "order service"() {
        given:
        String recipient = "example@micronaut.io"
        String orderNumber = UUID.randomUUID().toString()
        String text = "We have received your order ${orderNumber}. You will receive your product soon."
        String html = "<html><body><p>${text}</p></body></html>"

        when:
        orderService.sendOrderEmail(recipient, orderNumber)
        MailpitMessage message = client.getMessage("latest")

        then:
        message
        message.from()
        message.to()

        when:
        MailpitAddress from = message.from()
        List<MailpitAddress> to = message.to()

        then:
        fromConfiguration.from.email == from.address()
        [recipient] == to*.address()
        "Order Number: ${orderNumber}" == message.subject()
        text == message.text()
        html == message.html()
    }

    static class Mailpit {
        private static GenericContainer<?> container = new GenericContainer(DockerImageName.parse("axllent/mailpit"))
                .withExposedPorts(1025, 8025)
                .waitingFor(Wait.forHttp("/").forPort(8025))

        static Map<String, String> getProperties() {
            getProperties(getRunningContainer())
        }

        private static GenericContainer<?> getRunningContainer() {
            do {
                container.start()
            } while (!container.isRunning())
            container
        }

        static Map<String, String> getProperties(GenericContainer<?> container) {
            [
                    "javamail.properties.mail.smtp.host": container.getHost(),
                    "javamail.properties.mail.smtp.port": "${container.getMappedPort(1025)}",
                    "micronaut.http.services.mailpit.url": "http://${container.getHost()}:${container.getMappedPort(8025)}"
            ]
        }

        static void close() {
            container.close()
        }
    }
}
