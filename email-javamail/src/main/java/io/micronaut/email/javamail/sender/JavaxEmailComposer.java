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
package io.micronaut.email.javamail.sender;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Email;
import io.micronaut.email.EmailComposer;
import io.micronaut.email.EmailException;
import io.micronaut.email.javamail.composer.MessageComposer;
import jakarta.inject.Singleton;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Composes a {@link Message} given an {@link io.micronaut.email.Email}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Singleton
public class JavaxEmailComposer implements EmailComposer<Message> {
    private static final Logger LOG = LoggerFactory.getLogger(JavaxEmailComposer.class);
    private final SessionProvider sessionProvider;
    private final MessageComposer messageComposer;

    /**
     * @param sessionProvider Session Provider
     * @param messageComposer Message Composer
     */
    public JavaxEmailComposer(SessionProvider sessionProvider,
                              MessageComposer messageComposer) {
        this.sessionProvider = sessionProvider;
        this.messageComposer = messageComposer;
    }

    @Override
    @NonNull
    public Message compose(@NonNull @NotNull @Valid Email email) throws EmailException {
        Session session = sessionProvider.session();
        try {
            return messageComposer.compose(email, session);
        } catch (MessagingException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("MessagingException composing message", e);
            }
            throw new EmailException(e);
        }
    }
}
