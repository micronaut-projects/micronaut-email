package io.micronaut.email.mailpit.client;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.configuration.FromConfiguration;
import io.micronaut.email.mailpit.client.model.MailpitAddress;
import io.micronaut.email.mailpit.client.model.MailpitMessage;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "spec.name", value= "OrderServiceTest")
@Property(name = "micronaut.email.from.email", value= "info@micronaut.io")
@MicronautTest(startApplication = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceTest implements TestPropertyProvider {
    @Override
    public @NonNull Map<String, String> getProperties() {
        return Mailpit.getProperties();
    }

    @AfterAll
    void cleanupSpec() {
        Mailpit.close();
    }

    @Test
    void orderService(OrderService orderService, MailpitClient client, FromConfiguration fromConfiguration) {
        String recipient = "example@micronaut.io";
        String orderNumber = UUID.randomUUID().toString();
        String text = "We have received your order " + orderNumber + ". You will receive your product soon.";
        String html = "<html><body><p>" + text + "</p></body></html>";

        orderService.sendOrderEmail(recipient, orderNumber);

        MailpitMessage message = client.getMessage("latest");

        assertNotNull(message);
        assertNotNull(message.from());
        assertNotNull(message.to());
        MailpitAddress from = message.from();
        List<MailpitAddress> to = message.to();
        assertEquals(fromConfiguration.getFrom().getEmail(), from.address());
        assertEquals(List.of(recipient), to.stream().map(MailpitAddress::address).toList());
        assertEquals("Order Number: " + orderNumber, message.subject());
        assertEquals(text, message.text());
        assertEquals(html, message.html());
    }

    static class Mailpit {
        private Mailpit() {
        }

        private static GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("axllent/mailpit"))
                .withExposedPorts(1025, 8025)
                .waitingFor(Wait.forHttp("/").forPort(8025));

        public static Map<String, String> getProperties() {
            return getProperties(getRunningContainer());
        }

        private static GenericContainer<?> getRunningContainer() {
            org.junit.jupiter.api.Assumptions.assumeTrue(
                org.testcontainers.DockerClientFactory.instance().isDockerAvailable()
            );
            if (!container.isRunning()) {
                container.start();
            }
            return container;
        }

        public static Map<String, String> getProperties(GenericContainer<?> container) {
            return Map.of(
                    "javamail.properties.mail.smtp.host", container.getHost(),
                    "javamail.properties.mail.smtp.port", "" + container.getMappedPort(1025),
                    "micronaut.http.services.mailpit.url",
                    "http://"+ container.getHost() + ":" + container.getMappedPort(8025));
        }

        public static void close() {
            container.close();
        }
    }
}
