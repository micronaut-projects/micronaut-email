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
 * Mailpit message attachment metadata.
 *
 * @param partId Attachment part ID.
 * @param fileName File name.
 * @param contentType Content type.
 * @param contentId Content ID.
 * @param size Size in bytes.
 * @param checksums Attachment checksums.
 * @since 3.1.0
 */
@Serdeable
public record MailpitAttachment(
    @JsonProperty("PartID") @Nullable String partId,
    @JsonProperty("FileName") @Nullable String fileName,
    @JsonProperty("ContentType") @Nullable String contentType,
    @JsonProperty("ContentID") @Nullable String contentId,
    @JsonProperty("Size") long size,
    @JsonProperty("Checksums") @Nullable MailpitAttachmentChecksums checksums
) {
}
