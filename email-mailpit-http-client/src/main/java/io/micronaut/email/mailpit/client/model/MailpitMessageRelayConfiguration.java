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
import io.micronaut.core.annotation.Experimental;
import io.micronaut.serde.annotation.Serdeable;
import org.jspecify.annotations.Nullable;

/**
 * Mailpit message relay configuration.
 *
 * @param enabled Whether relay is enabled.
 * @param smtpServer Configured SMTP server.
 * @param returnPath Enforced return path.
 * @param allowedRecipients Allowed recipient pattern.
 * @param blockedRecipients Blocked recipient pattern.
 * @param overrideFrom Override From value.
 * @param preserveMessageIds Whether original Message-IDs are preserved.
 * @param recipientAllowlist Deprecated recipient allowlist value.
 * @since 3.1.0
 */
@Experimental
@Serdeable
public record MailpitMessageRelayConfiguration(
    @JsonProperty("Enabled") boolean enabled,
    @JsonProperty("SMTPServer") @Nullable String smtpServer,
    @JsonProperty("ReturnPath") @Nullable String returnPath,
    @JsonProperty("AllowedRecipients") @Nullable String allowedRecipients,
    @JsonProperty("BlockedRecipients") @Nullable String blockedRecipients,
    @JsonProperty("OverrideFrom") @Nullable String overrideFrom,
    @JsonProperty("PreserveMessageIDs") boolean preserveMessageIds,
    @JsonProperty("RecipientAllowlist") @Nullable String recipientAllowlist
) {
}
