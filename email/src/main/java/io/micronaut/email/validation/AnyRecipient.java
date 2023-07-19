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

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated type must have a recipient.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Constraint(validatedBy = AnyRecipientValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnyRecipient {
    /**
     * AnyRecipient message.
     */
    String MESSAGE = "io.micronaut.email.validation.AnyRecipient.message";

    /**
     * @return message The error message
     */
    String message() default "{" + MESSAGE + "}";

    /**
     * @return Groups to control the order in which constraints are evaluated,
     * or to perform validation of the partial state of a JavaBean.
     */
    Class<?>[] groups() default {};

    /**
     * @return Payloads used by validation clients to associate some metadata information with a given constraint declaration
     */
    Class<? extends Payload>[] payload() default {};

}

