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

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Introspected
public class Sender {
    @NonNull
    @NotNull
    @Valid
    private final Contact from;

    @Nullable
    @Valid
    private final Contact replyTo;

    /**
     *
     * @param from Sender of the Email
     */
    public Sender(@NonNull String from) {
        this(new Contact(from), null);
    }

    /**
     *
     * @param from Sender of the Email
     * @param replyTo Reply to
     */
    public Sender(@NonNull Contact from,
                  @Nullable Contact replyTo) {
        this.from = from;
        this.replyTo = replyTo;
    }

    /**
     *
     * @return Email sender
     */
    @NonNull
    public Contact getFrom() {
        return from;
    }

    /**
     *
     * @return Email Reply-to
     */
    @Nullable
    public Contact getReplyTo() {
        return replyTo;
    }
}
