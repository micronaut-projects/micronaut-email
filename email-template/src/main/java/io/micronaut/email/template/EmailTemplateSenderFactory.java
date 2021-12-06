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


import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.email.EmailSender;

/**
 * {@link Factory} which creates a {@link EmailTemplateSender} for each bean of type {@link EmailSender}.
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <H> HTML model
 * @param <T> Text model
 */
@Factory
public class EmailTemplateSenderFactory<H, T> {

    private final EmailRenderer<H, T> emailRenderer;

    /**
     *
     * @param emailRenderer Utility to render an email.
     */
    public EmailTemplateSenderFactory(EmailRenderer<H, T> emailRenderer) {
        this.emailRenderer = emailRenderer;
    }

    /**
     * Creates a {@link EmailTemplateSender} for each bean of type {@link EmailSender}.
     * @param emailSender Email Sender
     * @return A {@link DefaultEmailTemplateSender}.
     */
    @EachBean(EmailSender.class)
    public EmailTemplateSender<H, T> createEmailTemplateCourier(EmailSender emailSender) {
        return new DefaultEmailTemplateSender<>(emailRenderer, emailSender);
    }
}
