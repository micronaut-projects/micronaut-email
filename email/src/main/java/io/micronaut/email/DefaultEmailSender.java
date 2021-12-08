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
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Decorates with every {@link EmailDecorator} and send an email via a {@link TransactionalEmailSender}.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <R> Response Object
 */
public class DefaultEmailSender<R> implements EmailSender<R> {

    private final TransactionalEmailSender<R> transactionalEmailSender;
    private final List<EmailDecorator> decorators;

    /**
     *
     * @param transactionalEmailSender Transactional Email Sender
     * @param decorators Email decorators
     */
    public DefaultEmailSender(TransactionalEmailSender<R> transactionalEmailSender,
                              List<EmailDecorator> decorators) {
        this.transactionalEmailSender = transactionalEmailSender;
        this.decorators = decorators;
    }

    @Override
    @NonNull
    public Optional<R> send(@NonNull @NotNull Email email) {
        for (EmailDecorator decorator : decorators) {
            decorator.decorate(email);
        }
        return transactionalEmailSender.send(email);
    }

    @Override
    @NonNull
    public String getName() {
        return transactionalEmailSender.getName();
    }

    /**
     *
     * @return Whether tracking links is supported.
     */
    @Override
    public boolean isTrackingLinksSupported() {
        return transactionalEmailSender.isTrackingLinksSupported();
    }

    /**
     *
     * @return Whether sending attachments is supported
     */
    @Override
    public boolean isSendingAttachmentsSupported() {
        return transactionalEmailSender.isSendingAttachmentsSupported();
    }
}
