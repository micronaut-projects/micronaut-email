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
package io.micronaut.email.template;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.naming.Named;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

/**
 * Contract for sending an Email template.
 *
 * @author Sergio del Amo
 * @since 1.0
 * @param <H> HTML model
 * @param <T> Text model
 */
public interface EmailTemplateSender<H, T> extends Named {
    /**
     * Sends a template as a text email.
     * @param email Email
     */
    void send(@NonNull @NotNull @Valid Email<H, T>  email);

    /**
     * It builds and sends an email using the supplied builder.
     * @param email Email builder consumer
     */
    default void send(Consumer<Email.Builder<H, T>> email) {
        Email.Builder<H, T> builder = Email.builder();
        email.accept(builder);
        send(builder.build());
    }
}
