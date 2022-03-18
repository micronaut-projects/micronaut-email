/*
 * Copyright 2017-2022 original authors
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

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.AbstractTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.javamail.composer.MessageComposer;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * Java Mail implementation of {@link io.micronaut.email.TransactionalEmailSender} and {@link io.micronaut.email.AsyncTransactionalEmailSender}.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Named(JavaxEmailSender.NAME)
@Secondary
@Singleton
@Requires(beans = {SessionProvider.class, MessageComposer.class})
public class JavaxEmailSender extends AbstractTransactionalEmailSender<Message, Void> {
    /**
     * {@link JavaxEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "javaxmail";

    private static final Logger LOG = LoggerFactory.getLogger(JavaxEmailSender.class);

    private final JavaxEmailComposer javaxEmailComposer;

    /**
     * @param executorService    Executor service
     * @param javaxEmailComposer Message Composer
     */
    public JavaxEmailSender(@Named(TaskExecutors.IO) ExecutorService executorService,
                            JavaxEmailComposer javaxEmailComposer) {
        super(executorService);
        this.javaxEmailComposer = javaxEmailComposer;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public Void send(@NonNull @NotNull @Valid Email email,
                     @NonNull @NotNull Consumer<Message> emailRequest) throws EmailException {
        Message message = javaxEmailComposer.compose(email, emailRequest);
        try {
            Transport.send(message);
            return null;
        } catch (MessagingException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Message could not be sent to some or any of the recipients", e);
            }
            throw new EmailException(e);
        }
    }
}
