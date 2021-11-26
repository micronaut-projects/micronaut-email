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
import io.micronaut.email.EmailHeader;
import io.micronaut.views.ModelAndView;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Contract for sending an Email template.
 *
 * @author Sergio del Amo
 * @since 1.0
 * @param <T> The model type
 */
@FunctionalInterface
public interface EmailTemplateSender<T> {
    /**
     * Sends a template as a text email.
     * @param emailHeader Email Sender, recipients and subjects
     * @param text Emails Template's name and model for text
     * @param html Emails Template's name and model for html
     */
    void send(@NonNull @NotNull @Valid EmailHeader emailHeader,
              @Nullable ModelAndView<T> text,
              @Nullable ModelAndView<T> html);

    /**
     * Sends a template as a text email.
     * @param emailHeader Email Sender, recipients and subjects
     * @param text Emails Template's name and model for text
     */
    default void sendText(@NonNull @NotNull @Valid EmailHeader emailHeader,
              @NonNull ModelAndView<T> text) {
        send(emailHeader, text, null);
    }

    /**
     * Sends a template as a text email.
     * @param emailHeader Email Sender, recipients and subjects
     * @param html Emails Template's name and model for text
     */
    default void sendHtml(@NonNull @NotNull @Valid EmailHeader emailHeader,
                          @NonNull ModelAndView<T> html) {
        send(emailHeader, null, html);
    }
}
