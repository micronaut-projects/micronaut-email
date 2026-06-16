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
 * Represents a downloadable file attachment for an email.
 *
 * Instances are immutable and should be created via {@link Builder}.
 *
 * @author Vinit Shinde
 * @since 3.0.0
 */
@Introspected
public final class FileAttachment implements EmailAttachment {

    @NonNull
    @NotBlank
    private final String filename;

    @NonNull
    @NotNull
    private final byte[] content;

    @NonNull
    @NotBlank
    private final String contentType;

    /**
     * Internal constructor. Use {@link Builder} to create instances.
     *
     * @param filename the attachment filename
     * @param contentType the MIME content type
     * @param content the binary content
     */
    private FileAttachment(
        @NonNull String filename,
        @NonNull String contentType,
        @NonNull byte[] content) {
        this.filename = filename;
        this.contentType = contentType;
        this.content = content;
    }

    /**
     * Creates a new builder for {@link FileAttachment}.
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
     * FileAttachment builder for fluent construction.
     *
     * <p>Example:
     * <pre>
     * FileAttachment.builder()
     *     .filename("report.pdf")
     *     .contentType("application/pdf")
     *     .content(file)
     *     .build();
     * </pre>
     *
     * @author Vinit Shinde
     */
    public static class Builder {
        private String filename;
        private String contentType;
        private byte[] content;

        /**
         * Set the filename to be used when the attachment is downloaded.
         *
         * @param filename the download filename, must not be null
         * @return this builder
         */
        @NonNull
        public Builder filename(@NonNull String filename) {
            this.filename = filename;
            return this;
        }

        /**
         * Set the MIME content type for this attachment.
         *
         * @param contentType the MIME type, must not be null
         * @return this builder
         */
        @NonNull
        public Builder contentType(@NonNull String contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * Set the attachment content as a byte array.
         *
         * @param content the content bytes, must not be null
         * @return this builder
         */
        @NonNull
        public Builder content(@NonNull byte[] content) {
            this.content = content;
            return this;
        }

        /**
         * Read the content from a {@link File} and set it on the builder.
         *
         * @param file the file to read, must not be null
         * @return this builder
         * @throws IllegalArgumentException if the file cannot be read
         */
        @NonNull
        public Builder content(@NonNull File file) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
                byte[] bytes = new byte[(int) file.length()];
                dis.readFully(bytes);
                return content(bytes);
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not read file for attachment", e);
            }
        }

        /**
         * Read the content from an {@link InputStream} and set it on the builder.
         *
         * @param inputStream the input stream to read, must not be null
         * @return this builder
         * @throws IllegalArgumentException if the stream cannot be read
         */
        @NonNull
        public Builder content(@NonNull InputStream inputStream) {
            try {
                return content(inputStream.readAllBytes());
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not read input stream for attachment", e);
            }
        }

        /**
         * Build the immutable {@link FileAttachment} instance.
         *
         * @return a new FileAttachment
         * @throws NullPointerException if any required property is missing
         */
        @NonNull
        public FileAttachment build() {
            return new FileAttachment(
                Objects.requireNonNull(filename, "filename required"),
                Objects.requireNonNull(contentType, "contentType required"),
                Objects.requireNonNull(content, "content required")
            );
        }
    }
}
