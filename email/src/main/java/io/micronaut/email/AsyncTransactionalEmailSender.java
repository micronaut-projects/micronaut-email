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
import io.micronaut.core.naming.Named;
import org.reactivestreams.Publisher;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.function.Consumer;

/**
 * Contract to send emails Asynchronously.
 * Emails are validated.
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <I> Email Request
 * @param <O> Email Response
 */
public interface AsyncTransactionalEmailSender<I, O> extends Named {
    /**
     * Sends an email.
     * @param email Email
     * @return Response Object or empty optional if an error occurred
     * @throws EmailException Wrapper of any exception thrown while sending email
     */
    @NonNull
    @SingleResult
    default Publisher<O> sendAsync(@NonNull @NotNull @Valid Email email) throws EmailException {
        return sendAsync(email, i -> { });
    }

    /**
     * Sends an email.
     * @param email Email
     * @param emailRequest Email Request Consumer
     * @return Response Object or empty optional if an error occurred
     * @throws EmailException Wrapper of any exception thrown while sending email
     */
    @NonNull
    @SingleResult
    Publisher<O> sendAsync(@NonNull @NotNull @Valid Email email,
                           @NonNull @NotNull Consumer<I> emailRequest) throws EmailException;

}
