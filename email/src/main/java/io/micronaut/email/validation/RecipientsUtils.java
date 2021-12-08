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
package io.micronaut.email.validation;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.Contact;

import java.util.Collection;

/**
 * Utility class for {@link Recipients}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
public final class RecipientsUtils {
    /**
     *
     * @param recipients Email recipients
     * @return whether the recipients are valid or not
     */
    public static boolean isValid(@NonNull Recipients recipients) {
        return CollectionUtils.isNotEmpty(recipients.getTo()) ||
                CollectionUtils.isNotEmpty(recipients.getCc()) ||
                CollectionUtils.isNotEmpty(recipients.getBcc());
    }

    /**
     *
     * @param to To recipients
     * @param cc Carbon copy recipients
     * @param bcc Blind Carbon copy recipients
     * @return Email recipients
     */
    @NonNull
    public static Recipients create(@Nullable Collection<Contact> to,
                                    @Nullable Collection<Contact> cc,
                                    @Nullable Collection<Contact> bcc) {
        return new Recipients() {
            @Override
            @Nullable
            public Collection<Contact> getTo() {
                return to;
            }

            @Override
            @Nullable
            public Collection<Contact> getCc() {
                return cc;
            }

            @Override
            @Nullable
            public Collection<Contact> getBcc() {
                return bcc;
            }
        };
    }
}
