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

import io.micronaut.context.annotation.Factory;
import io.micronaut.core.util.StringUtils;
import io.micronaut.email.Email;
import jakarta.inject.Singleton;
import io.micronaut.validation.validator.constraints.ConstraintValidator;

/**
 * Builds {@link ConstraintValidator} for {@link AnyContent}, {@link io.micronaut.email.validation.AnyRecipient} for {@link Email}.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Factory
public class EmailFactory {
    /**
     * @return A {@link ConstraintValidator} implementation of a {@link AnyRecipient} validator for type {@link Email}
     */
    @Singleton
    public ConstraintValidator<AnyRecipient, Email> anyRecipientEmailConstraintValidator() {
        return (value, annotationMetadata, context) -> {
            if (value == null) {
                return true;
            }
            return RecipientsUtils.isValid(value);
        };
    }

    /**
     * @return A {@link ConstraintValidator} implementation of a {@link AnyContent} validator for type {@link Email}
     */
    @Singleton
    public ConstraintValidator<AnyContent, Email> anyContentEmailConstraintValidator() {
        return (value, annotationMetadata, context) -> {
            if (value == null) {
                return true;
            }
            return StringUtils.isNotEmpty(value.getHtml()) || StringUtils.isNotEmpty(value.getText());
        };
    }
}
