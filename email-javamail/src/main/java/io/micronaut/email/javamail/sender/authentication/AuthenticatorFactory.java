/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.email.javamail.sender.authentication;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

/**
 * Builds a {@link Authenticator} from configuration set at {@link JavaMailAuthenticationConfiguration}.
 *
 * @author Lukáš Moravec
 * @since 1.3.0
 */
@Factory
@Requires(beans = JavaMailAuthenticationConfiguration.class)
public class AuthenticatorFactory {

    /**
     * @param configuration JavaMail Authentication Configuration
     * @return An Authenticator
     */
    @Singleton
    @NonNull
    public Authenticator buildAuthenticator(JavaMailAuthenticationConfiguration configuration) {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(configuration.getUsername(), configuration.getPassword());
            }
        };
    }
}
