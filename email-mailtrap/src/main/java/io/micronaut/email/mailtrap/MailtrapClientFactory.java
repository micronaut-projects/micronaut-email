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

import io.mailtrap.client.MailtrapClient;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.annotation.Internal;
import jakarta.inject.Singleton;

/**
 * Creates a {@link MailtrapClient} intances.
 */
@Factory
@Internal
class MailtrapClientFactory {
    @Singleton
    MailtrapClient createClient(MailtrapConfiguration mailtrapConfiguration) {
        return io.mailtrap.factory.MailtrapClientFactory.createMailtrapClient(mailtrapConfiguration.getConfig().build());

    }
}
