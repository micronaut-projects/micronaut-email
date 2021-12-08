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
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.annotation.NonNull;

import java.util.List;

/**
 * Creates beans of type {@link EmailSender} with each bean of type {@link TransactionalEmailSender}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Factory
public class EmailSenderFactory {

    private final List<EmailDecorator> decorators;

    /**
     *
     * @param decorators Email decorators
     */
    public EmailSenderFactory(@NonNull List<EmailDecorator> decorators) {
        this.decorators = decorators;
    }

    /**
     *
     * @param sender Transactional Email Sender
     * @param <R> Return type
     * @return Email sender
     */
    @EachBean(TransactionalEmailSender.class)
    @NonNull
    public <R> EmailSender<R> createEmailSender(@NonNull TransactionalEmailSender<R> sender) {
        return new DefaultEmailSender<>(sender, decorators);
    }
}
