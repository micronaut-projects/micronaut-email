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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.email.Email;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;
import io.micronaut.validation.validator.constraints.ConstraintValidator;

/**
 * Builds {@link ConstraintValidator} for {@link io.micronaut.email.validation.AnyRecipient} for {@link Email}.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Requires(classes = ConstraintValidator.class)
@Singleton
public class AnyRecipientValidator implements ConstraintValidator<AnyRecipient, Email> {

    @Override
    public boolean isValid(@Nullable Email value, @NonNull AnnotationValue<AnyRecipient> annotationMetadata, @NonNull ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return RecipientsUtils.isValid(value);
    }
}
