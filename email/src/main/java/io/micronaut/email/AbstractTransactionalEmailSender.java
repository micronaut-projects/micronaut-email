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
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import jakarta.validation.constraints.NotNull;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * Abstract class which implements both {@link TransactionalEmailSender} and {@link AsyncTransactionalEmailSender}.
 * It provides an implemeentation of {@link AsyncTransactionalEmailSender#sendAsync(Email, Consumer)} by subscribing on an IO scheduler.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <I> Email Request
 * @param <O> Email Response
 */
public abstract class AbstractTransactionalEmailSender<I, O> implements AsyncTransactionalEmailSender<I, O>, TransactionalEmailSender<I, O> {

    private final Scheduler scheduler;

    /**
     *
     * @param executorService Executor service
     */
    protected AbstractTransactionalEmailSender(@Named(TaskExecutors.IO) ExecutorService executorService) {
        this.scheduler = Schedulers.fromExecutorService(executorService);
    }

    @Override
    @NonNull
    @SingleResult
    public Publisher<O> sendAsync(@NonNull @NotNull Email email,
                           @NonNull @NotNull Consumer<I> emailRequest) throws EmailException {
        return Mono.fromCallable(() -> send(email, emailRequest))
                .subscribeOn(scheduler);
    }
}
