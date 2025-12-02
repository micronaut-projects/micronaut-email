/*
 * Copyright 2017-2025 original authors
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

import java.util.Objects;

/**
 * Value type for an RFC 2392 Content-ID, used for inline email attachments.
 *
 * Instances represent the identifier part of a Content-ID header (without
 * surrounding angle brackets). Use {@link #toHeaderValue()} to obtain a header-safe
 * representation (with &lt; and &gt;).
 *
 * @since 3.0.0
 */
public record ContentId(@NonNull String value) {

    /**
     * Constructs a new {@code ContentId}.
     *
     * <p>The {@code value} must not be {@code null}. A {@link NullPointerException}
     * is thrown otherwise. This enforces the requirement that inline email
     * attachments define a non-null Content-ID.</p>
     *
     * @throws NullPointerException if {@code value} is {@code null}
     */
    public ContentId {
        Objects.requireNonNull(value, "ContentId value must not be null");
    }

    /**
     * Returns the formatted header value suitable for use in a Content-ID header.
     *
     * For example, a value of {@code "img-1"} will be returned as {@code "<img-1>"}.
     *
     * @return the header formatted content id, never {@code null}
     */
    @NonNull
    public String toHeaderValue() {
        return "<" + value + ">";
    }
}

