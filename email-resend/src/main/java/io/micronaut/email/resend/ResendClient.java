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
package io.micronaut.email.resend;

import io.micronaut.core.annotation.Internal;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.email.resend.model.ResendEmailRequest;
import io.micronaut.email.resend.model.ResendEmailResponse;
import io.micronaut.email.resend.model.ResendError;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import org.jspecify.annotations.Nullable;
import org.reactivestreams.Publisher;

/**
 * Declarative HTTP client for the Resend API.
 * @see <a href="https://resend.com/docs/api-reference/emails/send-email">Send Email</a>
 */
@Internal
@Client(id = ResendClient.SERVICE_ID, errorType = ResendError.class)
@Header(name = HttpHeaders.USER_AGENT, value = ResendClient.USER_AGENT)
interface ResendClient {
    /**
     * HTTP Client service ID.
     */
    String SERVICE_ID = "resend";

    /**
     * Resend API URL.
     */
    String DEFAULT_URL = "https://api.resend.com";

    /**
     * Resend rejects requests without a User-Agent HTTP header.
     */
    String USER_AGENT = "micronaut-email-resend";

    /**
     * Idempotency key HTTP header name.
     */
    String HEADER_IDEMPOTENCY_KEY = "Idempotency-Key";

    /**
     * Sends an email.
     *
     * @param authorization Authorization HTTP header value
     * @param idempotencyKey Idempotency key
     * @param request Send email request
     * @return Send email response
     */
    @Post("/emails")
    @Nullable
    ResendEmailResponse send(@Header(HttpHeaders.AUTHORIZATION) String authorization,
                             @Nullable @Header(HEADER_IDEMPOTENCY_KEY) String idempotencyKey,
                             @Body ResendEmailRequest request);

    /**
     * Sends an email without blocking.
     *
     * @param authorization Authorization HTTP header value
     * @param idempotencyKey Idempotency key
     * @param request Send email request
     * @return Send email response
     */
    @Post("/emails")
    @SingleResult
    Publisher<ResendEmailResponse> sendAsync(@Header(HttpHeaders.AUTHORIZATION) String authorization,
                                             @Nullable @Header(HEADER_IDEMPOTENCY_KEY) String idempotencyKey,
                                             @Body ResendEmailRequest request);
}
