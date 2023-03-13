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
package io.micronaut.email.mailjet;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.function.Consumer;

/**
 * <a href="https://www.mailjet.com">Mailjet</a> implementation of {@link io.micronaut.email.TransactionalEmailSender} and {@link io.micronaut.email.AsyncTransactionalEmailSender}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Named(MailjetEmailSender.NAME)
@Requires(beans = { MailjetConfiguration.class, MailjetEmailComposer.class })
@Singleton
public class MailjetEmailSender implements TransactionalEmailSender<MailjetRequest, MailjetResponse>,
        AsyncTransactionalEmailSender<MailjetRequest, MailjetResponse> {
    /**
     * {@link MailjetEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "mailjet";

    private static final Logger LOG = LoggerFactory.getLogger(MailjetEmailSender.class);

    private final MailjetClient mailjetClient;
    private final MailjetEmailComposer mailjetEmailComposer;

    /**
     * @param mailjetConfiguration Mailjet Configuration.
     * @param mailjetEmailComposer Mailjet Request Composer
     */
    public MailjetEmailSender(MailjetConfiguration mailjetConfiguration,
                              MailjetEmailComposer mailjetEmailComposer) {
        this.mailjetEmailComposer = mailjetEmailComposer;
        ClientOptions clientOptions = ClientOptions.builder()
                .apiKey(mailjetConfiguration.getApiKey())
                .apiSecretKey(mailjetConfiguration.getApiSecret())
                .build();
        this.mailjetClient = new MailjetClient(clientOptions);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public MailjetResponse send(@NonNull @NotNull @Valid Email email,
                                @NonNull @NotNull Consumer<MailjetRequest> emailRequest) throws EmailException {
        MailjetRequest request = mailjetEmailComposer.compose(email, emailRequest);
        try {
            MailjetResponse response = mailjetClient.post(request);
            if (LOG.isTraceEnabled()) {
                LOG.trace("response status: {}", response.getStatus());
                LOG.trace("response data: {}", response.getData().toString());
            }
        return response;
        } catch (MailjetException e) {
            throw new EmailException(e);
        }
    }

    @Override
    @NonNull
    public Publisher<MailjetResponse> sendAsync(@NonNull @NotNull @Valid Email email,
                                                @NonNull @NotNull Consumer<MailjetRequest> emailRequest) throws EmailException {
        MailjetRequest request = mailjetEmailComposer.compose(email, emailRequest);
        return Mono.fromFuture(mailjetClient.postAsync(request));
    }
}
