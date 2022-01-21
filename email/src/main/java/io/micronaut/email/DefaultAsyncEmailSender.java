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
import io.micronaut.core.async.annotation.SingleResult;
import org.reactivestreams.Publisher;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Consumer;

/**
 * Decorates with every {@link EmailDecorator} and send an email via a {@link AsyncTransactionalEmailSender}.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <I> Email Request
 * @param <O> Email Response
 */
@EachBean(AsyncTransactionalEmailSender.class)
public class DefaultAsyncEmailSender<I, O> implements AsyncEmailSender<I, O> {

    private final AsyncTransactionalEmailSender<I, O> transactionalEmailSender;
    private final List<EmailDecorator> decorators;

    /**
     *
     * @param transactionalEmailSender Transactional Email Sender
     * @param decorators Email decorators
     */
    public DefaultAsyncEmailSender(AsyncTransactionalEmailSender<I, O> transactionalEmailSender,
                                   List<EmailDecorator> decorators) {
        this.transactionalEmailSender = transactionalEmailSender;
        this.decorators = decorators;
    }

    @Override
    @NonNull
    @SingleResult
    public Publisher<O> sendAsync(@NonNull @NotNull Email.Builder emailBuilder, @NonNull @NotNull Consumer<I> emailRequest) throws EmailException {
        for (EmailDecorator decorator : decorators) {
            decorator.decorate(emailBuilder);
        }
        return transactionalEmailSender.sendAsync(emailBuilder.build(), emailRequest);
    }

    @Override
    @NonNull
    public String getName() {
        return transactionalEmailSender.getName();
    }
}
