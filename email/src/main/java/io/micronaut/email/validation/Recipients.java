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
import io.micronaut.email.Contact;
import java.util.Collection;

/**
 * Email Recipients. To, carbon copy and blind carbon copy recipients.
 * @author Sergio del Amo
 * @since 1.0.0
 */
public interface Recipients {

    /**
     *
     * @return Email recipients.
     */
    @Nullable
    Collection<Contact> getTo();

    /**
     *
     * @return Email carbon copy recipients.
     */
    @Nullable
    Collection<Contact> getCc();

    /**
     *
     * @return Email blind carbon copy recipients.
     */
    @Nullable
    Collection<Contact> getBcc();

    /**
     *
     * @return Email recipients
     */
    @NonNull
    default Recipients getRecipients() {
        return RecipientsUtils.create(getTo(), getCc(), getBcc());
    }
}
