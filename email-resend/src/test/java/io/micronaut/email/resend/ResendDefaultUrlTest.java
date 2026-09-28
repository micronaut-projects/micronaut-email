package io.micronaut.email.resend;

import io.micronaut.context.ApplicationContext;
import io.micronaut.discovery.ServiceInstance;
import io.micronaut.discovery.ServiceInstanceList;
import io.micronaut.http.client.LoadBalancer;
import io.micronaut.http.client.LoadBalancerResolver;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResendDefaultUrlTest {

    @Test
    void resendApiIsTheDefaultUrl() {
        try (ApplicationContext ctx = ApplicationContext.run(Map.of("resend.api-key", "re_xxx"))) {
            assertEquals(List.of(URI.create("https://api.resend.com")), resendUris(ctx));
        }
    }

    @Test
    void httpClientResolvesTheDefaultUrl() {
        try (ApplicationContext ctx = ApplicationContext.run(Map.of("resend.api-key", "re_xxx"))) {
            LoadBalancer loadBalancer = ctx.getBean(LoadBalancerResolver.class)
                .resolve(ResendClient.SERVICE_ID)
                .orElseThrow();

            ServiceInstance serviceInstance = Mono.from(loadBalancer.select()).block();

            assertNotNull(serviceInstance);
            assertEquals(URI.create("https://api.resend.com"), serviceInstance.getURI());
        }
    }

    @Test
    void httpServiceConfigurationDoesNotRequireAUrl() {
        try (ApplicationContext ctx = ApplicationContext.run(Map.of(
            "resend.api-key", "re_xxx",
            "micronaut.http.services.resend.read-timeout", "5s"))) {
            assertEquals(List.of(URI.create("https://api.resend.com")), resendUris(ctx));
        }
    }

    @Test
    void httpServiceUrlReplacesTheDefaultUrl() {
        try (ApplicationContext ctx = ApplicationContext.run(Map.of(
            "resend.api-key", "re_xxx",
            "micronaut.http.services.resend.url", "http://localhost:8025"))) {
            assertEquals(List.of(URI.create("http://localhost:8025")), resendUris(ctx));
        }
    }

    private static List<URI> resendUris(ApplicationContext ctx) {
        return ctx.getBeansOfType(ServiceInstanceList.class)
            .stream()
            .filter(list -> ResendClient.SERVICE_ID.equals(list.getID()))
            .flatMap(list -> list.getInstances().stream())
            .map(ServiceInstance::getURI)
            .toList();
    }
}
