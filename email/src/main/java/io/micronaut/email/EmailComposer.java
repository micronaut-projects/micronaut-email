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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.function.Consumer;

/**
 * Composes an Email Request for the Transactional Email provider given a {@link Email}.
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <I> Email Request
 */
@FunctionalInterface
public interface EmailComposer<I> {
    @NonNull
    I compose(@NonNull @NotNull @Valid Email email) throws EmailException;

    @NonNull
    default I compose(@NonNull @NotNull @Valid Email email,
              Consumer<I> emailRequest) throws EmailException {
        I result = compose(email);
        emailRequest.accept(result);
        return result;
    }
}
