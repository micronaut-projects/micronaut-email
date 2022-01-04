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
package io.micronaut.email.javamail;

import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

/**
 * Creates a {@link Message} for the given {@link Email}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@DefaultImplementation(DefaultMessageComposer.class)
@FunctionalInterface
public interface MessageComposer {
    /**
     *
     * @param email Email
     * @param session Session Object
     * @return A Message
     * @throws MessagingException when creating Message
     */
    @NonNull
    Message compose(@NonNull Email email,
                    @NonNull Session session) throws MessagingException;
}
