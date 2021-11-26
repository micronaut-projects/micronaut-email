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
package io.micronaut.email.mailjet;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import javax.validation.constraints.NotBlank;

/**
 * {@link ConfigurationProperties} implementation of {@link MailjetConfiguration}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Requires(property = MailjetConfigurationProperties.PREFIX + ".api-key")
@Requires(property = MailjetConfigurationProperties.PREFIX + ".api-secret")
@ConfigurationProperties(MailjetConfigurationProperties.PREFIX)
public class MailjetConfigurationProperties implements MailjetConfiguration {
    /**
     * mailjet prefix.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String PREFIX = "mailjet";

    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_ENABLED = true;

    /**
     * The default version.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String DEFAULT_VERSION = "v3.1";

    private boolean enabled = DEFAULT_ENABLED;

    @NonNull
    @NotBlank
    private String version = DEFAULT_VERSION;

    @NonNull
    @NotBlank
    private String apiKey;

    @NonNull
    @NotBlank
    private String apiSecret;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * If Mailjet integration is enabled. Default value: `{@value #DEFAULT_ENABLED}`
     *
     * @param enabled True if security is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    @NonNull
    public String getVersion() {
        return version;
    }

    /**
     * Mailjet API Version. Default value: `{@value #DEFAULT_VERSION}`
     * @param version Mailjet API Version
     */
    public void setVersion(@NonNull String version) {
        this.version = version;
    }

    @Override
    @NonNull
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Mailjet API Key.
     * @param apiKey Mailjet API Key.
     */
    public void setApiKey(@NonNull String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    @NonNull
    public String getApiSecret() {
        return apiSecret;
    }

    /**
     * Mailjet API Secret.
     * @param apiSecret Mailjet API Secret
     */
    public void setApiSecret(@NonNull String apiSecret) {
        this.apiSecret = apiSecret;
    }
}
