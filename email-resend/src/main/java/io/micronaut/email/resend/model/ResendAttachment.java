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
package io.micronaut.email.resend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import org.jspecify.annotations.Nullable;

/**
 * An attachment of a Resend email. Either {@code content} or {@code path} is required.
 *
 * @param content Base64-encoded attachment content.
 * @param filename File name.
 * @param path URL where the attachment is hosted.
 * @param contentType Content type. Resend derives it from the file name if not set.
 * @param contentId Content-ID to embed the attachment inline with {@code cid:}.
 * @since 3.3.0
 */
@Serdeable
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ResendAttachment(
    @JsonProperty("content") @Nullable String content,
    @JsonProperty("filename") @Nullable String filename,
    @JsonProperty("path") @Nullable String path,
    @JsonProperty("content_type") @Nullable String contentType,
    @JsonProperty("content_id") @Nullable String contentId
) {
}
