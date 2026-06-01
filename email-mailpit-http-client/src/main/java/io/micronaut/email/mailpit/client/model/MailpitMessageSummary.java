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

import java.util.List;

/**
 * Mailpit message summary.
 *
 * @param id Database ID.
 * @param messageId Message ID.
 * @param read Read status.
 * @param from From address.
 * @param to To addresses.
 * @param cc Cc addresses.
 * @param bcc Bcc addresses.
 * @param replyTo Reply-To addresses.
 * @param subject Subject.
 * @param created Received date.
 * @param username Authentication username.
 * @param tags Tags.
 * @param size Size in bytes.
 * @param attachments Number of attachments.
 * @param snippet Message snippet.
 * @since 3.1.0
 */
@Experimental
@Serdeable
public record MailpitMessageSummary(
    @JsonProperty("ID") @Nullable String id,
    @JsonProperty("MessageID") @Nullable String messageId,
    @JsonProperty("Read") boolean read,
    @JsonProperty("From") @Nullable MailpitAddress from,
    @JsonProperty("To") @Nullable List<MailpitAddress> to,
    @JsonProperty("Cc") @Nullable List<MailpitAddress> cc,
    @JsonProperty("Bcc") @Nullable List<MailpitAddress> bcc,
    @JsonProperty("ReplyTo") @Nullable List<MailpitAddress> replyTo,
    @JsonProperty("Subject") @Nullable String subject,
    @JsonProperty("Created") @Nullable String created,
    @JsonProperty("Username") @Nullable String username,
    @JsonProperty("Tags") @Nullable List<String> tags,
    @JsonProperty("Size") long size,
    @JsonProperty("Attachments") int attachments,
    @JsonProperty("Snippet") @Nullable String snippet
) {
}
