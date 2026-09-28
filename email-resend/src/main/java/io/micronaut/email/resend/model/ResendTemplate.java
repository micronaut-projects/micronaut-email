/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.email.resend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import org.jspecify.annotations.Nullable;

import java.util.Map;

/**
 * A published Resend template. It cannot be combined with an HTML or text body.
 *
 * @param id Template ID or alias.
 * @param variables Template variables. Values are strings or numbers.
 * @since 3.3.0
 */
@Serdeable
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ResendTemplate(
    @JsonProperty("id") String id,
    @JsonProperty("variables") @Nullable Map<String, Object> variables
) {
}
