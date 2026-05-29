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

import java.util.List;

/**
 * Mailpit message details.
 *
 * @param id Database ID.
 * @param messageId Message ID.
 * @param from From address.
 * @param to To addresses.
 * @param cc Cc addresses.
 * @param bcc Bcc addresses.
 * @param replyTo Reply-To addresses.
 * @param returnPath Return-Path value.
 * @param subject Subject.
 * @param listUnsubscribe List-Unsubscribe summary.
 * @param date Message date.
 * @param tags Tags.
 * @param username Authentication username.
 * @param text Text body.
 * @param html HTML body.
 * @param size Size in bytes.
 * @param inline Inline attachments.
 * @param attachments Attachments.
 * @since 3.1.0
 */
@Serdeable
public record MailpitMessage(
    @JsonProperty("ID") @Nullable String id,
    @JsonProperty("MessageID") @Nullable String messageId,
    @JsonProperty("From") @Nullable MailpitAddress from,
    @JsonProperty("To") @Nullable List<MailpitAddress> to,
    @JsonProperty("Cc") @Nullable List<MailpitAddress> cc,
    @JsonProperty("Bcc") @Nullable List<MailpitAddress> bcc,
    @JsonProperty("ReplyTo") @Nullable List<MailpitAddress> replyTo,
    @JsonProperty("ReturnPath") @Nullable String returnPath,
    @JsonProperty("Subject") @Nullable String subject,
    @JsonProperty("ListUnsubscribe") @Nullable MailpitListUnsubscribe listUnsubscribe,
    @JsonProperty("Date") @Nullable String date,
    @JsonProperty("Tags") @Nullable List<String> tags,
    @JsonProperty("Username") @Nullable String username,
    @JsonProperty("Text") @Nullable String text,
    @JsonProperty("HTML") @Nullable String html,
    @JsonProperty("Size") long size,
    @JsonProperty("Inline") @Nullable List<MailpitAttachment> inline,
    @JsonProperty("Attachments") @Nullable List<MailpitAttachment> attachments
) {
}
