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
package io.micronaut.email.postmark;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import javax.validation.constraints.NotBlank;

/**
 * {@link ConfigurationProperties} implementation of {@link PostmarkConfiguration}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Requires(property = PostmarkConfigurationProperties.PREFIX + ".api-token")
@ConfigurationProperties(PostmarkConfigurationProperties.PREFIX)
public class PostmarkConfigurationProperties implements PostmarkConfiguration {
    /**
     * postmark prefix.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String PREFIX = "postmark";

    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_ENABLED = true;

    private boolean enabled = DEFAULT_ENABLED;

    @NonNull
    @NotBlank
    private String apiToken;

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
     * @return Postmark API token.
     */
    @Override
    @NonNull
    public String getApiToken() {
        return apiToken;
    }

    /**
     * Postmark API token.
     * @param apiToken Postmark API token.
     */
    public void setApiToken(@NonNull String apiToken) {
        this.apiToken = apiToken;
    }
}
