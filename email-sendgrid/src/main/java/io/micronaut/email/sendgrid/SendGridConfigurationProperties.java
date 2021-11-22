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
package io.micronaut.email.sendgrid;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import javax.validation.constraints.NotBlank;

/**
 * {@link ConfigurationProperties} implementation of {@link SendGridConfiguration}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Requires(property = SendGridConfigurationProperties.PREFIX + ".api-key")
@ConfigurationProperties(SendGridConfigurationProperties.PREFIX)
public class SendGridConfigurationProperties implements SendGridConfiguration {
    /**
     * sendgrid prefix.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String PREFIX = "sendgrid";
    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_ENABLED = true;

    @NonNull
    @NotBlank
    private String apiKey;

    private boolean enabled = DEFAULT_ENABLED;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * If Security is enabled. Default value {@value #DEFAULT_ENABLED}
     *
     * @param enabled True if security is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     *
     * @return The API (Application Programming Interface) keys to authenticate access to SendGrid service.
     */
    @Override
    @NonNull
    public String getApiKey() {
        return apiKey;
    }

    /**
     *
     * @param apiKey The API (Application Programming Interface) keys to authenticate access to SendGrid service.
     */
    public void setApiKey(@NonNull String apiKey) {
        this.apiKey = apiKey;
    }
}
