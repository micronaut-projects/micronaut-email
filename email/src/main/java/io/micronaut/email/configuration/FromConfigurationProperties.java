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

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.email.Contact;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * {@link ConfigurationProperties} implementation of {@link FromConfiguration}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Requires(property = FromConfigurationProperties.PREFIX + ".email")
@ConfigurationProperties(FromConfigurationProperties.PREFIX)
public class FromConfigurationProperties implements FromConfiguration {

    /**
     * From configuration prefix.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String PREFIX = "micronaut.email.from";

    @NonNull
    @NotBlank
    @Email
    private String email;

    @Nullable
    private String name;

    /**
     *
     * @return email address of the contact sending the email.
     */
    @NonNull
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email From email address.
     */
    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    /**
     *
     * @return name of the contact sending the email.
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     *
     * @param name From name
     */
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Override
    @NonNull
    public Contact getFrom() {
        return getName() == null ?
                new Contact(getEmail()) :
                new Contact(getEmail(), getName());
    }
}
