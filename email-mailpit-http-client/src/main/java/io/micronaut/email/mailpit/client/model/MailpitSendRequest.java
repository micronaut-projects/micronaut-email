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
import java.util.Map;

/**
 * Request for Mailpit's HTTP send API.
 *
 * @param from From address.
 * @param to To recipients.
 * @param cc Cc recipients.
 * @param bcc Bcc email addresses.
 * @param replyTo Reply-To recipients.
 * @param subject Subject.
 * @param text Text body.
 * @param html HTML body.
 * @param attachments Attachments.
 * @param tags Tags.
 * @param headers Custom headers.
 * @since 3.1.0
 */
@Serdeable
public record MailpitSendRequest(
    @JsonProperty("From") MailpitSendAddress from,
    @JsonProperty("To") @Nullable List<MailpitSendAddress> to,
    @JsonProperty("Cc") @Nullable List<MailpitSendAddress> cc,
    @JsonProperty("Bcc") @Nullable List<String> bcc,
    @JsonProperty("ReplyTo") @Nullable List<MailpitSendAddress> replyTo,
    @JsonProperty("Subject") @Nullable String subject,
    @JsonProperty("Text") @Nullable String text,
    @JsonProperty("HTML") @Nullable String html,
    @JsonProperty("Attachments") @Nullable List<MailpitSendAttachment> attachments,
    @JsonProperty("Tags") @Nullable List<String> tags,
    @JsonProperty("Headers") @Nullable Map<String, String> headers
) {
}
