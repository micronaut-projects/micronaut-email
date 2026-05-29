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
 * Mailpit message list response.
 *
 * @param total Total mailbox messages.
 * @param unread Total unread mailbox messages.
 * @param count Legacy current page message count.
 * @param messagesCount Total messages matching the current query.
 * @param messagesUnread Total unread messages matching the current query.
 * @param start Pagination offset.
 * @param tags All current tags.
 * @param messages Message summaries.
 * @since 3.1.0
 */
@Serdeable
public record MailpitMessagesSummary(
    @JsonProperty("total") long total,
    @JsonProperty("unread") long unread,
    @JsonProperty("count") long count,
    @JsonProperty("messages_count") long messagesCount,
    @JsonProperty("messages_unread") long messagesUnread,
    @JsonProperty("start") int start,
    @JsonProperty("tags") @Nullable List<String> tags,
    @JsonProperty("messages") @Nullable List<MailpitMessageSummary> messages
) {
}
