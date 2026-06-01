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

import java.util.Map;

/**
 * Mailpit application information.
 *
 * @param version Current Mailpit version.
 * @param latestVersion Latest available Mailpit version.
 * @param database Database path.
 * @param databaseSize Database size in bytes.
 * @param messages Total message count.
 * @param unread Total unread message count.
 * @param tags Message totals by tag.
 * @param runtimeStats Runtime statistics.
 * @since 3.1.0
 */
@Experimental
@Serdeable
public record MailpitAppInformation(
    @JsonProperty("Version") @Nullable String version,
    @JsonProperty("LatestVersion") @Nullable String latestVersion,
    @JsonProperty("Database") @Nullable String database,
    @JsonProperty("DatabaseSize") long databaseSize,
    @JsonProperty("Messages") long messages,
    @JsonProperty("Unread") long unread,
    @JsonProperty("Tags") @Nullable Map<String, Long> tags,
    @JsonProperty("RuntimeStats") @Nullable MailpitRuntimeStats runtimeStats
) {
}
