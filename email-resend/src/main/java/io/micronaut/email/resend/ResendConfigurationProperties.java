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

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Internal;
import jakarta.validation.constraints.NotBlank;

/**
 * {@link ConfigurationProperties} implementation of {@link ResendConfiguration}.
 */
@Requires(property = ResendConfiguration.PREFIX + ".api-key")
@ConfigurationProperties(ResendConfiguration.PREFIX)
@Internal
class ResendConfigurationProperties implements ResendConfiguration {
    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_ENABLED = true;

    private boolean enabled = DEFAULT_ENABLED;

    @NotBlank
    @SuppressWarnings("NullAway.Init")
    private String apiKey;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * If Resend integration is enabled. Default value: `{@value #DEFAULT_ENABLED}`
     *
     * @param enabled True if Resend integration is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Resend API key.
     *
     * @param apiKey Resend API key.
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
