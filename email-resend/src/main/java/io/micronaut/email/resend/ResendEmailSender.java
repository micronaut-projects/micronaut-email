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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.TransactionalEmailSender;
import io.micronaut.email.resend.model.ResendEmailRequest;
import io.micronaut.email.resend.model.ResendEmailResponse;
import io.micronaut.email.resend.model.ResendError;
import io.micronaut.http.HttpHeaderValues;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jspecify.annotations.Nullable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * <a href="https://resend.com">Resend</a> implementation of {@link TransactionalEmailSender} and {@link AsyncTransactionalEmailSender}.
 */
@Requires(beans = { ResendConfiguration.class, ResendEmailComposer.class, ResendClient.class })
@Named(ResendEmailSender.NAME)
@Singleton
@Internal
class ResendEmailSender implements TransactionalEmailSender<ResendEmailRequest.Builder, ResendEmailResponse>,
    AsyncTransactionalEmailSender<ResendEmailRequest.Builder, ResendEmailResponse> {
    /**
     * {@link ResendEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "resend";

    private static final Logger LOG = LoggerFactory.getLogger(ResendEmailSender.class);
    private static final String EMPTY_RESPONSE = "Resend responded without an email ID";

    private final ResendConfiguration configuration;
    private final ResendClient client;
    private final ResendEmailComposer composer;

    ResendEmailSender(ResendConfiguration configuration,
                      ResendClient client,
                      ResendEmailComposer composer) {
        this.configuration = configuration;
        this.client = client;
        this.composer = composer;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ResendEmailResponse send(Email email, Consumer<ResendEmailRequest.Builder> emailRequest) throws EmailException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Sending email to {}", email.getTo());
        }
        ResendEmailRequest.Builder builder = composer.compose(email, emailRequest);
        ResendEmailResponse response;
        try {
            response = client.send(authorization(), builder.getIdempotencyKey(), builder.build());
        } catch (HttpClientException e) {
            throw emailException(e);
        }
        if (response == null) {
            throw new EmailException(EMPTY_RESPONSE);
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("Email ID: {}", response.id());
        }
        return response;
    }

    @Override
    @SingleResult
    public Publisher<ResendEmailResponse> sendAsync(Email email, Consumer<ResendEmailRequest.Builder> emailRequest) throws EmailException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Sending email to {}", email.getTo());
        }
        ResendEmailRequest.Builder builder = composer.compose(email, emailRequest);
        return Mono.from(client.sendAsync(authorization(), builder.getIdempotencyKey(), builder.build()))
            .onErrorMap(HttpClientException.class, ResendEmailSender::emailException)
            .switchIfEmpty(Mono.error(() -> new EmailException(EMPTY_RESPONSE)));
    }

    private String authorization() {
        return HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER + " " + configuration.getApiKey();
    }

    private static EmailException emailException(HttpClientException e) {
        if (e instanceof HttpClientResponseException responseException) {
            int statusCode = responseException.getStatus().getCode();
            ResendError error = responseException.getResponse().getBody(ResendError.class).orElse(null);
            return new ResendEmailException(message(statusCode, error), statusCode, error, e);
        }
        return new EmailException(e);
    }

    private static String message(int statusCode, @Nullable ResendError error) {
        StringBuilder sb = new StringBuilder("Resend responded with HTTP ").append(statusCode);
        if (error != null) {
            if (error.name() != null) {
                sb.append(" (").append(error.name()).append(')');
            }
            if (error.message() != null) {
                sb.append(": ").append(error.message());
            }
        }
        return sb.toString();
    }
}
