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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Class representing the person who receives an email.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Introspected
public class Contact {
    @NotNull
    @NotBlank
    @Email
    private final String email;

    @Nullable
    private final  String name;

    /**
     *
     * @param email Contact's email.
     */
    public Contact(@NonNull String email) {
        this(email, null);
    }

    /**
     *
     * @param email Recipient's email.
     * @param name Recipient's name.
     */
    public Contact(@NonNull String email, @Nullable String name) {
        this.name = name;
        this.email = email;
    }

    /**
     *
     * @return Recipient's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return Recipient's name
     */
    @Nullable
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Contact contact = (Contact) o;

        if (!Objects.equals(email, contact.email)) {
            return false;
        }
        return Objects.equals(name, contact.name);
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
