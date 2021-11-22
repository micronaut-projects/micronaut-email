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
import java.util.List;

/**
 * Email Recipient.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Introspected
public class Recipient {

    @NonNull
    @NotNull
    @Valid
    private final List<Contact> to;

    @Nullable
    @Valid
    private final List<Contact> cc;

    @Nullable
    @Valid
    private final List<Contact> bcc;

    /**
     *
     * @param to Email's recipient
     * @param cc Carbon copy
     * @param bcc Blind Carbon copy
     */
    public Recipient(@NonNull List<Contact> to,
                     @Nullable List<Contact> cc,
                     @Nullable List<Contact> bcc) {
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
    }

    /**
     *
     * @return Email recipients.
     */
    @NonNull
    public List<Contact> getTo() {
        return to;
    }

    /**
     *
     * @return Email carbon copy recipients.
     */
    @Nullable
    public List<Contact> getCc() {
        return cc;
    }

    /**
     *
     * @return Email blind carbon copy recipients.
     */
    @Nullable
    public List<Contact> getBcc() {
        return bcc;
    }
}
