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
import org.jspecify.annotations.NonNull;
import io.micronaut.core.util.Toggleable;

/**
 * Mailtrap configuration.
 */
public interface MailtrapConfiguration extends Toggleable {

    /**
     * mailtrap prefix.
     */
    String PREFIX = "mailtrap";

    /**
     * mailtrap prefix.
     */
    String PROPERTY_ENABLED = PREFIX + ".enabled";

    /**
     *
     * @return Mailtrap configuration.
     */
    MailtrapConfig.@NonNull Builder getConfig();
}
