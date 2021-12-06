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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import java.util.List;

/**
 * @author Sergio del Amo
 * @since 1.0.0
 */
public interface EmailWithoutContent extends Recipients {
    /**
     *
     * @return Email sender
     */
    @NonNull
    Contact getFrom();

    /**
     *
     * @return Email Reply-to
     */
    @Nullable
    Contact getReplyTo();

    /**
     *
     * @return Email's subject
     */
    @NonNull
    String getSubject();

    /**
     *
     * @return Whether to track if the email is opened
     */
    boolean getTrackOpens();

    /**
     *
     * @return Whether to track the email's links
     */
    @Nullable
    TrackLinks getTrackLinks();

    /**
     *
     * @return Email attachments
     */
    @Nullable
    List<Attachment> getAttachments();
}