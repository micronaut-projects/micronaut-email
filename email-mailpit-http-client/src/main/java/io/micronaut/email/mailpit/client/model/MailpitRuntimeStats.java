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

/**
 * Mailpit runtime statistics.
 *
 * @param uptime Server uptime in seconds.
 * @param memory Current memory usage in bytes.
 * @param messagesDeleted Runtime deleted message count.
 * @param smtpAccepted Accepted SMTP message count.
 * @param smtpAcceptedSize Accepted SMTP message size in bytes.
 * @param smtpRejected Rejected SMTP message count.
 * @param smtpIgnored Ignored SMTP message count.
 * @since 3.1.0
 */
@Experimental
@Serdeable
public record MailpitRuntimeStats(
    @JsonProperty("Uptime") long uptime,
    @JsonProperty("Memory") long memory,
    @JsonProperty("MessagesDeleted") long messagesDeleted,
    @JsonProperty("SMTPAccepted") long smtpAccepted,
    @JsonProperty("SMTPAcceptedSize") long smtpAcceptedSize,
    @JsonProperty("SMTPRejected") long smtpRejected,
    @JsonProperty("SMTPIgnored") long smtpIgnored
) {
}
