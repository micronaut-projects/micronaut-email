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
package io.micronaut.email.javamail.sender;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.exceptions.DisabledBeanException;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.util.Properties;

/**
 * Builds a {@link MailPropertiesProvider} if the properties are set at {@link JavaMailConfiguration}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Factory
public class MailPropertiesProviderFactory {

    /**
     *
     * @param javaMailConfiguration JavaMail Configuration
     * @return A MailPropertiesProvider
     * @throws DisabledBeanException if JavaMail configuration does not contain any properties
     */
    @Singleton
    @NonNull
    public MailPropertiesProvider buildMailPropertiesProvider(JavaMailConfiguration javaMailConfiguration) {
        if (javaMailConfiguration.getProperties() == null || javaMailConfiguration.getProperties().isEmpty()) {
            throw new DisabledBeanException("JavaMail configuration does not contain any properties");
        }
        return () -> {
            Properties properties = new Properties();
            properties.putAll(javaMailConfiguration.getProperties());
            return properties;
        };
    }
}
