/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.email.resend;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Internal;
import io.micronaut.discovery.ServiceInstanceList;
import io.micronaut.discovery.StaticServiceInstanceList;
import io.micronaut.http.client.ServiceHttpClientConfiguration;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.net.URI;
import java.util.List;

/**
 * Points the {@value ResendClient#SERVICE_ID} HTTP Client to the Resend API unless the application configures a URL for the service.
 */
@Factory
@Internal
final class ResendServiceInstanceListFactory {
    private static final String SERVICE_PREFIX = ServiceHttpClientConfiguration.PREFIX + "." + ResendClient.SERVICE_ID;

    @Requires(missingProperty = SERVICE_PREFIX + ".url")
    @Requires(missingProperty = SERVICE_PREFIX + ".urls")
    @Named(ResendClient.SERVICE_ID)
    @Singleton
    ServiceInstanceList resendServiceInstanceList() {
        return new StaticServiceInstanceList(ResendClient.SERVICE_ID, List.of(URI.create(ResendClient.DEFAULT_URL)));
    }
}
