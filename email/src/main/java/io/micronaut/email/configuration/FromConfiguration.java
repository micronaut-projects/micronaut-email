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
package io.micronaut.email.configuration;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Contact;

/**
 * A bean of type {@link FromConfiguration} defines the default sender.
 * It will be used by {@link io.micronaut.email.FromDecorator} to populate {@link io.micronaut.email.Email#getFrom()} if not specified.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@FunctionalInterface
public interface FromConfiguration {

    /**
     *
     * @return Contact of the person sending the email.
     */
    @NonNull
    Contact getFrom();
}
