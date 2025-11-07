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

package io.micronaut.email.sendgrid;

import com.sendgrid.SendGrid;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

/**
 * Builds a {@link SendGrid} from configuration set at {@link SendGridConfiguration}.
 *
 * @author Daniel Muhra
 */
@Factory
@Requires(beans = SendGridConfiguration.class)
public class SendGridFactory {

    /**
     * @param configuration SendGrid Configuration
     * @return a sendgrid instance.
     */
    @Singleton
    @NonNull
    public SendGrid buildSendGrid(SendGridConfiguration configuration) {
        return new SendGrid(configuration.getApiKey());
    }
}
