/*
 * Copyright 2017-2025 original authors
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

package io.micronaut.email;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * Represents an inline attachment for HTML emails. Use {@link ContentId} for CID URLs.
 *
 * @author Vinit Shinde
 * @since 3.0.0
 */
@Introspected
public final class InlineAttachment implements EmailAttachment {

    @NonNull
    @NotBlank
    private final String filename;

    @NonNull
    @NotNull
    private final byte[] content;

    @NonNull
    @NotBlank
    private final String contentType;

    @NonNull
    private final ContentId contentId;

    /**
     * Creates a new {@link InlineAttachment} for embedding content within an HTML email.
     * Typically used via the builder API.
     *
     * @param filename    the name of the file to display in the email client.
     * @param contentType the MIME content type of the attachment (e.g. "image/png").
     * @param content     the attachment data as a byte array.
     * @param contentId   a unique content ID for referencing in HTML content ("cid:" URLs).
     */
    private InlineAttachment(
        @NonNull String filename,
        @NonNull String contentType,
        @NonNull byte[] content,
        @NonNull ContentId contentId
    ) {
        this.filename = filename;
        this.contentType = contentType;
        this.content = content;
        this.contentId = contentId;
    }

    /**
     * Creates a builder for {@link InlineAttachment}.
     *
     * @return The builder instance.
     */
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the filename to show up in the email.
     *
     * @return filename to show up in email
     */
    @Override
    @NonNull
    public String getFilename() {
        return filename;
    }

    /**
     * Returns the file content as bytes.
     *
     * @return file content bytes
     */
    @Override
    @NonNull
    public byte[] getContent() {
        return content;
    }

    /**
     * Returns the MIME content type for the file.
     *
     * @return file content type
     */
    @Override
    @NonNull
    public String getContentType() {
        return contentType;
    }

    /**
     * Returns the RFC 2392 content ID (for 'cid:' URL in HTML).
     *
     * @return the ContentId used for inline references
     */
    @NonNull
    public ContentId getContentId() {
        return contentId;
    }


     /**
     * Builder for {@link InlineAttachment}.
     *
     * Example:
     * <pre>
     * InlineAttachment.builder()
     *     .filename("logo.png")
     *     .contentType("image/png")
     *     .content(imageBytes)
     *     .contentId("logo123")
     *     .build();
     * </pre>
     *
     * @author Vinit Shinde
     */
    public static class Builder {
        private String filename;
        private String contentType;
        private byte[] content;
        private ContentId contentId;

        /**
         *
         * @param filename filename to show up in email
         * @return InlineAttachment's builder
         */
        @NonNull
        public Builder filename(@NonNull String filename) {
            this.filename = filename;
            return this;
        }

        /**
         *
         * @param contentType file content type
         * @return InlineAttachment's builder
         */
        @NonNull
        public Builder contentType(@NonNull String contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         *
         * @param content file content
         * @return Attachment's builder
         */
        @NonNull
        public Builder content(@NonNull byte[] content) {
            this.content = content;
            return this;
        }

        /**
         *
         * @param file file
         * @return InlineAttachment's builder
         */
        @NonNull
        public Builder content(@NonNull File file) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
                byte[] bytes = new byte[(int) file.length()];
                dis.readFully(bytes);
                return content(bytes);
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not read file for inline attachment", e);
            }
        }

        /**
         *
         * @param inputStream Content's inputStream
         * @return InlineAttachment's builder
         */
        @NonNull
        public Builder content(@NonNull InputStream inputStream) {
            try {
                return content(inputStream.readAllBytes());
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not read input stream for inline attachment", e);
            }
        }

        /**
         * Sets the content ID.
         * @param contentId the ContentId object
         * @return this builder
         */
        @NonNull
        public Builder contentId(@NonNull ContentId contentId) {
            this.contentId = contentId;
            return this;
        }

        /**
         * Sets the content ID from a string value (convenience method).
         * @param contentId the identifier value (must be unique per email)
         * @return this builder
         */
        @NonNull
        public Builder contentId(@NonNull String contentId) {
            this.contentId = new ContentId(contentId);
            return this;
        }

        /**
         *
         * @return an InlineAttachment.
         */
        @NonNull
        public InlineAttachment build() {
            return new InlineAttachment(
                Objects.requireNonNull(filename, "filename required"),
                Objects.requireNonNull(contentType, "contentType required"),
                Objects.requireNonNull(content, "content required"),
                Objects.requireNonNull(contentId, "contentId required")
            );
        }
    }
}
