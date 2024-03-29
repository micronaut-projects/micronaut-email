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
import io.micronaut.core.order.Ordered;

import jakarta.validation.constraints.NotNull;

/**
 * Decorates every Email send by an {@link EmailSender} or an {@link AsyncEmailSender}.
 * For example, you can use a decorator to add a bcc recipient to every email you send.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@FunctionalInterface
public interface EmailDecorator extends Ordered {

    /**
     * Populates an email.
     * @param emailBuilder Email Builder
     */
    void decorate(@NonNull @NotNull Email.Builder emailBuilder);
}
