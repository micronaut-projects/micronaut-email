package io.micronaut.email.docs;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.context.env.Environment;
import io.micronaut.context.env.PropertySource;
import io.micronaut.email.test.Mailpit;
import org.testcontainers.DockerClientFactory;

import java.util.Map;

/**
 * Supplies the SMTP and HTTP API properties of the shared Mailpit test container to the Python tests
 * run with the {@code mailpit} environment, like {@code TestPropertyProvider} does for the Java, Kotlin
 * and Groovy suites.
 * <p>
 * The configurer is written in Java because Micronaut Test calls {@code TestPropertyProvider} before the
 * application context, and with it the GraalPy runtime, exists, so a Python test class cannot supply
 * the container properties. It uses the {@link #configure(ApplicationContext)} callback because the
 * {@link io.micronaut.context.ApplicationContextBuilder} is configured before {@code @MicronautTest}
 * selects the environments, so the {@code mailpit} environment can only be checked on the built context.
 */
@ContextConfigurer
public class MailpitTestConfigurer implements ApplicationContextConfigurer {

    private static final String ENVIRONMENT = "mailpit";

    @Override
    public void configure(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        if (environment.getActiveNames().contains(ENVIRONMENT) && DockerClientFactory.instance().isDockerAvailable()) {
            environment.addPropertySource(PropertySource.of(ENVIRONMENT, Map.copyOf(Mailpit.getProperties())));
        }
    }
}
