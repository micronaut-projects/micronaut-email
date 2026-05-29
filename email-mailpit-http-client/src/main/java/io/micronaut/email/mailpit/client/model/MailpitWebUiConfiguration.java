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
package io.micronaut.email.mailpit.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import org.jspecify.annotations.Nullable;

/**
 * Mailpit web UI configuration.
 *
 * @param label Optional instance label.
 * @param messageRelay Message relay configuration.
 * @param spamAssassin Whether SpamAssassin is enabled.
 * @param chaosEnabled Whether chaos support is enabled.
 * @param duplicatesIgnored Whether duplicate messages are ignored.
 * @param hideDeleteAllButton Whether the delete all button is hidden.
 * @since 3.1.0
 */
@Serdeable
public record MailpitWebUiConfiguration(
    @JsonProperty("Label") @Nullable String label,
    @JsonProperty("MessageRelay") @Nullable MailpitMessageRelayConfiguration messageRelay,
    @JsonProperty("SpamAssassin") boolean spamAssassin,
    @JsonProperty("ChaosEnabled") boolean chaosEnabled,
    @JsonProperty("DuplicatesIgnored") boolean duplicatesIgnored,
    @JsonProperty("HideDeleteAllButton") boolean hideDeleteAllButton
) {
}
