package io.micronaut.email.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class Mailpit {
    private Mailpit() {
    }

    private static GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("axllent/mailpit"))
        .withExposedPorts(1025, 8025)
        .waitingFor(Wait.forHttp("/").forPort(8025));

    public static Map<String, String> getProperties() {
        return getProperties(getRunningContainer());
    }

    private static GenericContainer<?> getRunningContainer() {
        if (!container.isRunning()) {
            container.start();
        }
        return container;
    }

    public static Map<String, Object> getJavaMailProperties() {
        return getJavaMailProperties(getRunningContainer());
    }

    public static Map<String, Object> getJavaMailProperties(GenericContainer<?> container) {
        return Map.of(
            "mail.smtp.host", container.getHost(),
            "mail.smtp.port", container.getMappedPort(1025));
    }

    public static Map<String, String> getProperties(GenericContainer<?> container) {
        return Map.of(
            "micronaut.http.services.mailpit.url",
            "http://"+ container.getHost() + ":" + container.getMappedPort(8025));
    }

    public static void close() {
        container.close();
    }
}
