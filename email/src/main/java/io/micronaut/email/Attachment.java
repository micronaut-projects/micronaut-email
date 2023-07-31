/*
 * Copyright 2017-2021 original authors
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

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Email's Attachment.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Introspected
public class Attachment {

    @NonNull
    @NotBlank
    private final String filename;

    @NonNull
    @NotNull
    private final byte[] content;

    @NonNull
    @NotBlank
    private final String contentType;

    @Nullable
    private final String id;

    @Nullable
    private final String disposition;

    /**
     *
     * @param filename filename to show up in email
     * @param content file content
     * @param contentType file content type
     * @param id content identifier
     * @param disposition content disposition
     * @since 2.1.0
     */
    @Creator
    public Attachment(@NonNull String filename,
                      @NonNull String contentType,
                      @NonNull byte[] content,
                      @Nullable String id,
                      @Nullable String disposition) {
        this.filename = filename;
        this.content = content;
        this.contentType = contentType;
        this.id = id;
        this.disposition = disposition;
    }

    /**
     *
     * @param filename filename to show up in email
     * @param content file content
     * @param contentType file content type
     * @param id content identifier
     * @deprecated Use {@link Attachment#Attachment(String, String, byte[], String, String)}
     */
    @Deprecated(since = "2.1.0", forRemoval = true)
    public Attachment(@NonNull String filename,
                      @NonNull String contentType,
                      @NonNull byte[] content,
                      @Nullable String id) {
        this(filename, contentType, content, id, null);
    }

    /**
     *
     * @return Attachment's builder
     */
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     *
     * @return filename to show up in email
     */
    @NonNull
    public String getFilename() {
        return this.filename;
    }

    /**
     *
     * @return file content type
     */
    @NonNull
    public String getContentType() {
        return this.contentType;
    }

    /**
     *
     * @return file content type
     */
    @NonNull
    public byte[] getContent() {
        return this.content;
    }

    /**
     *
     * @return Content Id
     */
    @Nullable
    public String getId() {
        return this.id;
    }

    /**
     *
     * @return Content Disposition
     * @since 2.1.0
     */
    @Nullable
    public String getDisposition() {
        return this.disposition;
    }

    /**
     * Attachment's builder.
     */
    public static class Builder {
        private String filename;
        private byte[] content;
        private String contentType;
        private String id;
        private String disposition;

        /**
         *
         * @param filename filename to show up in email
         * @return Attachment's builder
         */
        @NonNull
        public Builder filename(@NonNull String filename) {
            this.filename = filename;
            return this;
        }

        /**
         *
         * @param contentType file content type
         * @return Attachment's builder
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
         * @return Attachment's builder
         */
        @NonNull
        public Builder content(@NonNull File file) {
            try {
                if (file.length() == 0L) {
                    return content(new FileInputStream(file));
                }
                byte[] bytes = new byte[(int) file.length()];
                DataInputStream dis = new DataInputStream(new FileInputStream(file));
                dis.readFully(bytes);
                return content(bytes);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("Could not read attachment file to bytes");
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException while reading attachment file to bytes");
            }
        }

        /**
         *
         * @param inputStream Content's inputStream
         * @return Attachment's builder
         */
        @NonNull
        public Builder content(@NonNull InputStream inputStream) {
            try {
                //TODO use the following once JDK 11 is the minimum
                //return content(inputStream.readAllBytes());
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[4];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                byte[] result = buffer.toByteArray();
                buffer.close();
                return content(result);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("Could not read attachment file to bytes");
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException while reading attachment file to bytes");
            }
        }

        /**
         *
         * @param id content identifier
         * @return Attachment's builder
         */
        @NonNull
        public Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        /**
         *
         * @param disposition content disposition
         * @return Attachment's builder
         */
        @NonNull
        public Builder disposition(@NonNull String disposition) {
            this.disposition = disposition;
            return this;
        }

        /**
         *
         * @return an Attachment.
         */
        @NonNull
        public Attachment build() {
            return new Attachment(Objects.requireNonNull(filename),
                    Objects.requireNonNull(contentType),
                    Objects.requireNonNull(content),
                    id,
                    disposition);
        }
    }
}
