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

import io.micronaut.context.annotation.EachBean;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Consumer;

/**
 * Decorates with every {@link EmailDecorator} and send an email via a {@link TransactionalEmailSender}.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <I> Email Request
 * @param <O> Email Response
 */
@EachBean(TransactionalEmailSender.class)
public class DefaultEmailSender<I, O> implements EmailSender<I, O> {

    private final TransactionalEmailSender<I, O> transactionalEmailSender;
    private final List<EmailDecorator> decorators;

    /**
     *
     * @param transactionalEmailSender Transactional Email Sender
     * @param decorators Email decorators
     */
    public DefaultEmailSender(TransactionalEmailSender<I, O> transactionalEmailSender,
                              List<EmailDecorator> decorators) {
        this.transactionalEmailSender = transactionalEmailSender;
        this.decorators = decorators;
    }

    @Override
    @NonNull
    public O send(@NonNull @NotNull Email.Builder emailBuilder, @NonNull @NotNull Consumer<I> emailRequest) throws EmailException {
        for (EmailDecorator decorator : decorators) {
            decorator.decorate(emailBuilder);
        }
        return transactionalEmailSender.send(emailBuilder.build(), emailRequest);
    }

    @Override
    @NonNull
    public String getName() {
        return transactionalEmailSender.getName();
    }
}
