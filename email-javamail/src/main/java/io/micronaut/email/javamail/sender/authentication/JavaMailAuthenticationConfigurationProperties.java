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

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.email.javamail.sender.JavaMailConfigurationProperties;

import javax.validation.constraints.NotBlank;

/**
 * {@link ConfigurationProperties} implementation of {@link JavaMailAuthenticationConfiguration}.
 *
 * @author Lukáš Moravec
 * @since 1.3.0
 */
@Requires(property = JavaMailAuthenticationConfigurationProperties.PREFIX)
@Requires(property = JavaMailAuthenticationConfigurationProperties.PREFIX + ".enabled", notEquals = StringUtils.FALSE)
@ConfigurationProperties(JavaMailAuthenticationConfigurationProperties.PREFIX)
public class JavaMailAuthenticationConfigurationProperties implements JavaMailAuthenticationConfiguration {

    /**
     * Authentication configuration prefix.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String PREFIX = JavaMailConfigurationProperties.PREFIX + ".authentication";

    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_ENABLED = false;

    private boolean enabled = DEFAULT_ENABLED;

    @NonNull
    @NotBlank
    private String username;

    @NonNull
    @NotBlank
    private String password;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * If authentication is enabled. Default value: `{@value #DEFAULT_ENABLED}`
     *
     * @param enabled True if authentication is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return Authentication username.
     */
    @NonNull
    public String getUsername() {
        return username;
    }

    /**
     * Authentication username.
     *
     * @param username Authentication username..
     */
    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    /**
     * @return Authentication password.
     */
    @NonNull
    public String getPassword() {
        return password;
    }

    /**
     * Authentication password.
     *
     * @param password Authentication password..
     */
    public void setPassword(@NonNull String password) {
        this.password = password;
    }
}
