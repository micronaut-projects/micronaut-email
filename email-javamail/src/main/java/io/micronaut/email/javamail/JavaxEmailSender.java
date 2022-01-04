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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Email;
import io.micronaut.email.TransactionalEmailSender;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Optional;

/**
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Named(JavaxEmailSender.NAME)
@Singleton
@Requires(beans = { SessionProvider.class, MessageComposer.class})
public class JavaxEmailSender implements TransactionalEmailSender<Void> {
    /**
     * {@link JavaxEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "javaxmail";

    private static final Logger LOG = LoggerFactory.getLogger(JavaxEmailSender.class);

    private final SessionProvider sessionProvider;
    private final MessageComposer messageComposer;

    /**
     *
     * @param sessionProvider Session Provider
     * @param messageComposer Message Composer
     */
    public JavaxEmailSender(SessionProvider sessionProvider,
                            MessageComposer messageComposer) {
        this.sessionProvider = sessionProvider;
        this.messageComposer = messageComposer;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public Optional<Void> send(@NonNull Email email) {
        Optional<Message> messageOptional = composeMessage(email);
        if (!messageOptional.isPresent()) {
            return Optional.empty();
        }
        Message message = messageOptional.get();
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Message could not be sent to some or any of the recipients", e);
            }
        }
        return Optional.empty();
    }

    @NonNull
    private Optional<Message> composeMessage(@NonNull Email email) {
        Session session = sessionProvider.session();
        try {
            return Optional.of(messageComposer.compose(email, session));
        } catch (MessagingException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("MessagingException composing message", e);
            }
        }
        return Optional.empty();

    }
}
