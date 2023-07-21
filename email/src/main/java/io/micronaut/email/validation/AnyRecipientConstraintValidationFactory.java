/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.email.Email;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import jakarta.inject.Singleton;

/**
 * Builds {@link ConstraintValidator} for {@link io.micronaut.email.validation.AnyRecipient} for {@link Email}.
 * @deprecated {@link AnyRecipientValidator} is used instead. The {@link @Factory} annotation was intentionally removed. Thus, this class does nothing.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Deprecated(forRemoval = true, since = "4.0.2")
public class AnyRecipientConstraintValidationFactory {
    @Singleton
    public ConstraintValidator<AnyRecipient, Email> anyRecipientEmailConstraintValidator() {
        return (value, annotationMetadata, context) -> false;
    }
}
