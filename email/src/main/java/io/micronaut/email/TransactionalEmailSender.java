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
import io.micronaut.core.naming.Named;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * API to be implemented by third party transactional email providers.
 * Emails are validated.
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <R> Response Object
 */
public interface TransactionalEmailSender<R> extends Named {

    /**
     * Sends an email.
     * @param email Email
     * @return Response Object or empty optional if an error occurred
     */
    @NonNull
    Optional<R> send(@NonNull @NotNull @Valid Email email);

    /**
     *
     * @return Whether tracking links is supported.
     */
    default boolean isTrackingLinksSupported() {
        return false;
    }

    /**
     *
     * @return Whether sending attachments is supported
     */
    default boolean isSendingAttachmentsSupported() {
        return false;
    }
}
