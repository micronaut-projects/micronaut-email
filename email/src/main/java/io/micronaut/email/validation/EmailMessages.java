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
package io.micronaut.email.validation;

import io.micronaut.context.StaticMessageSource;
import jakarta.inject.Singleton;

/**
 * Validation messages for Email.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Singleton
public class EmailMessages extends StaticMessageSource {

    /**
     * {@link AnyRecipient} message.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String ANY_RECIPIENT_MESSAGE = "You have to specify to, cc or a bcc recipient";

    /**
     * {@link AnyContent} message.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String ANY_CONTENT_MESSAGE = "You have to specify text, html or both";
    /**
     * The message suffix to use.
     */
    private static final String MESSAGE_SUFFIX = ".message";

    /**
     * Default constructor to initialize messages.
     * via {@link #addMessage(String, String)}
     */
    public EmailMessages() {
        addMessage(AnyRecipient.class.getName() + MESSAGE_SUFFIX, ANY_RECIPIENT_MESSAGE);
        addMessage(AnyContent.class.getName() + MESSAGE_SUFFIX, ANY_CONTENT_MESSAGE);
    }
}
