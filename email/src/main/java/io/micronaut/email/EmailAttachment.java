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

import io.micronaut.core.annotation.NonNull;

/**
 * Strongly typed email attachment. Use {@link FileAttachment} for regular files,
 * or {@link InlineAttachment} for embedded inline content.
 *
 * @author Vinit Shinde
 * @since 3.0.0
 */
public sealed interface EmailAttachment
    permits FileAttachment, InlineAttachment {

    /**
     * The filename as shown in the client's download/save dialogs.
     *
     * @return the filename
     */
    @NonNull
    String getFilename();

    /**
     * The attachment's content as bytes.
     *
     * @return the content bytes
     */
    @NonNull
    byte[] getContent();

    /**
     * The MIME type for the content.
     *
     * @return the MIME type
     */
    @NonNull
    String getContentType();
}

