/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.email;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.configuration.FromConfiguration;
import jakarta.inject.Singleton;

import jakarta.validation.constraints.NotNull;

/**
 * Decorates an email by setting the from field if not specified with the value provided by {@link FromConfiguration}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Requires(beans = FromConfiguration.class)
@Singleton
public class FromDecorator implements EmailDecorator {
    private final FromConfiguration fromConfiguration;

    /**
     *
     * @param fromConfiguration From configuration
     */
    public FromDecorator(FromConfiguration fromConfiguration) {
        this.fromConfiguration = fromConfiguration;
    }

    @Override
    public void decorate(@NonNull @NotNull Email.Builder emailBuilder) {
        if (!emailBuilder.getFrom().isPresent()) {
            emailBuilder.from(fromConfiguration.getFrom());
        }
    }
}
