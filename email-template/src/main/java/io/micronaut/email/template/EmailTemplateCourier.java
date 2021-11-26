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
package io.micronaut.email.template;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.email.Recipient;
import io.micronaut.email.Sender;
import io.micronaut.views.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Contract for sending an Email template.
 *
 * @author Sergio del Amo
 * @since 1.0
 * @param <T> The model type
 */
@FunctionalInterface
public interface EmailTemplateCourier<T> {
    /**
     * Sends a template as a text email.
     * @param sender Email's Sender (from, replyTo emails).
     * @param recipient Email's Recipient (to, cc, bcc)
     * @param subject Email's subject
     * @param text Emails Template's name and model for text
     * @param html Emails Template's name and model for html
     */
    void send(@NonNull @NotNull @Valid Sender sender,
              @NonNull @NotNull @Valid Recipient recipient,
              @NonNull @NotBlank String subject,
              @Nullable ModelAndView<T> text,
              @Nullable ModelAndView<T> html);

    /**
     * Sends a template as a text email.
     * @param sender Email's Sender (from, replyTo emails).
     * @param recipient Email's Recipient (to, cc, bcc)
     * @param subject Email's subject
     * @param text Emails Template's name and model for text
     */
    default void sendText(@NonNull @NotNull @Valid Sender sender,
              @NonNull @NotNull @Valid Recipient recipient,
              @NonNull @NotBlank String subject,
              @NonNull ModelAndView<T> text) {
        send(sender, recipient, subject, text, null);
    }

    /**
     * Sends a template as a text email.
     * @param sender Email's Sender (from, replyTo emails).
     * @param recipient Email's Recipient (to, cc, bcc)
     * @param subject Email's subject
     * @param html Emails Template's name and model for text
     */
    default void sendHtml(@NonNull @NotNull @Valid Sender sender,
                          @NonNull @NotNull @Valid Recipient recipient,
                          @NonNull @NotBlank String subject,
                          @NonNull ModelAndView<T> html) {
        send(sender, recipient, subject, null, html);
    }
}
