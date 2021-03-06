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

import java.util.Optional;

/**
 * Email body backed by a String.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 */
public class StringBody implements Body {

    @NonNull
    private final String text;
    private final BodyType bodyType;

    public StringBody(@NonNull String text) {
        this(text, BodyType.TEXT);
    }

    public StringBody(@NonNull String text, BodyType bodyType) {
        this.text = text;
        this.bodyType = bodyType;
    }

    @Override
    @NonNull
    public Optional<String> get(@NonNull BodyType bodyType) {
        return this.bodyType == bodyType ? Optional.of(text) : Optional.empty();
    }
}
