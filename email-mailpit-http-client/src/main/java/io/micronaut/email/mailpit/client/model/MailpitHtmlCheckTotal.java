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
package io.micronaut.email.mailpit.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

/**
 * Mailpit HTML compatibility total.
 *
 * @param tests Total tests.
 * @param nodes Total HTML nodes.
 * @param supported Supported percentage.
 * @param partial Partially supported percentage.
 * @param unsupported Unsupported percentage.
 * @since 3.1.0
 */
@Serdeable
public record MailpitHtmlCheckTotal(
    @JsonProperty("Tests") int tests,
    @JsonProperty("Nodes") int nodes,
    @JsonProperty("Supported") double supported,
    @JsonProperty("Partial") double partial,
    @JsonProperty("Unsupported") double unsupported
) {
}
