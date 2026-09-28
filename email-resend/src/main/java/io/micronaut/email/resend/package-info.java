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
/**
 * Classes related with Resend integration.
 * @see <a href="https://resend.com/docs/api-reference/emails/send-email">Resend Send Email API</a>
 *
 * @since 3.3.0
 */
@Requires(property = PROPERTY_ENABLED, value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Configuration
@NullMarked
package io.micronaut.email.resend;

import io.micronaut.context.annotation.Configuration;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import org.jspecify.annotations.NullMarked;

import static io.micronaut.email.resend.ResendConfiguration.PROPERTY_ENABLED;
