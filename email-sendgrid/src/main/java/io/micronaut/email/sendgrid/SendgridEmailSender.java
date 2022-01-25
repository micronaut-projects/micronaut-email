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
package io.micronaut.email.sendgrid;

import com.sendgrid.APICallback;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.TransactionalEmailSender;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <a href="https://sendgrid.com">SendGrid</a> implementation of {@link io.micronaut.email.TransactionalEmailSender} and {@link io.micronaut.email.AsyncTransactionalEmailSender} .
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Requires(beans = { SendGridConfiguration.class, SendgridEmailComposer.class })
@Named(SendgridEmailSender.NAME)
@Singleton
public class SendgridEmailSender implements TransactionalEmailSender<Request, Response>,
        AsyncTransactionalEmailSender<Request, Response> {
    /**
     * {@link SendgridEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "sendgrid";
    private static final Logger LOG = LoggerFactory.getLogger(SendgridEmailSender.class);

    private final SendGrid sendGrid;
    private final SendgridEmailComposer sendgridEmailComposer;

    /**
     * @param sendGridConfiguration SendGrid Configuration
     * @param sendgridEmailComposer SendGrid Email composer
     */
    public SendgridEmailSender(SendGridConfiguration sendGridConfiguration,
                               SendgridEmailComposer sendgridEmailComposer) {
        sendGrid = new SendGrid(sendGridConfiguration.getApiKey());
        this.sendgridEmailComposer = sendgridEmailComposer;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public Response send(@NonNull @NotNull @Valid Email email,
                         @NonNull @NotNull Consumer<Request> emailRequest) throws EmailException {
        try {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Sending email to {}", email.getTo());
            }
            return send(sendgridEmailComposer.compose(email, emailRequest));
        } catch (IOException ex) {
            throw new EmailException(ex);
        }
    }

    private Response send(@NonNull Request request) throws IOException {
        Response response = sendGrid.api(request);
        if (LOG.isInfoEnabled()) {
            LOG.info("Status Code: {}", String.valueOf(response.getStatusCode()));
            LOG.info("Body: {}", response.getBody());
            LOG.info("Headers {}", response.getHeaders()
                    .keySet()
                    .stream()
                    .map(key -> key.toString() + "=" + response.getHeaders().get(key))
                    .collect(Collectors.joining(", ", "{", "}")));
        }
        return response;
    }

    @Override
    @NonNull
    public Publisher<Response> sendAsync(@NonNull @NotNull @Valid Email email,
                                         @NonNull @NotNull Consumer<Request> emailRequest) throws EmailException {
        return Mono.create(callback -> {
            sendGrid.attempt(sendgridEmailComposer.compose(email, emailRequest), new APICallback() {
                @Override
                public void error(Exception ex) {
                    callback.error(ex);
                }

                @Override
                public void response(Response response) {
                    callback.success(response);
                }
            });
        });
    }
}
