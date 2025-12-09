/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.email.mailtrap;

import io.mailtrap.config.MailtrapConfig;
import io.micronaut.context.annotation.ConfigurationBuilder;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Internal;
import org.jspecify.annotations.NonNull;

@ConfigurationProperties(MailtrapConfiguration.PREFIX)
@Internal
class MailtrapConfigurationProperties implements MailtrapConfiguration {
    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_ENABLED = true;

    private boolean enabled = DEFAULT_ENABLED;

    @ConfigurationBuilder(prefixes = "")
    private final MailtrapConfig.Builder config = new MailtrapConfig.Builder();

    @Override
    public MailtrapConfig.@NonNull Builder getConfig() {
        return config;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * If Mailtrap integration is enabled. Default value true
     *
     * @param enabled True if security is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
