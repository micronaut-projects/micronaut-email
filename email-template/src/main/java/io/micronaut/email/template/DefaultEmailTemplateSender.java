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
import io.micronaut.email.Email;
import io.micronaut.email.EmailHeader;
import io.micronaut.email.EmailSender;
import io.micronaut.views.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Renders an email and sends it.
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <T> The model type
 */
public class DefaultEmailTemplateSender<T> implements EmailTemplateSender<T> {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEmailTemplateSender.class);
    private final EmailRenderer<T> emailRenderer;
    private final EmailSender emailSender;

    /**
     *
     * @param emailRenderer Utility to render an email.
     * @param emailSender The Email Sender
     */
    public DefaultEmailTemplateSender(EmailRenderer<T> emailRenderer,
                                      EmailSender emailSender) {
        this.emailRenderer = emailRenderer;
        this.emailSender = emailSender;
    }

    @Override
    public void send(@NonNull @NotNull @Valid EmailHeader emailHeader,
                          @Nullable ModelAndView<T> text,
                          @Nullable ModelAndView<T> html) {
        Email email = emailRenderer.render(emailHeader, text, html);
        emailSender.send(email);
    }
}
