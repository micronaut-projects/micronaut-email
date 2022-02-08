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
package io.micronaut.email.ses;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;
import software.amazon.awssdk.services.ses.model.SesRequest;
import software.amazon.awssdk.services.ses.model.SesResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

/**
 * <a href="https://aws.amazon.com/es/ses/">Amazon Simple Email Service</a> implementation of @link io.micronaut.email.AsyncTransactionalEmailSender}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Named(AsyncSesEmailSender.NAME)
@Requires(beans = SesAsyncClient.class)
@Singleton
public class AsyncSesEmailSender implements AsyncTransactionalEmailSender<SesRequest, SesResponse> {
    /**
     * {@link AsyncSesEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "ses";

    private static final Logger LOG = LoggerFactory.getLogger(AsyncSesEmailSender.class);

    private final SesAsyncClient ses;
    private final SesEmailComposer messageComposer;

    /**
     * @param ses Amazon Simple Email Service Client
     * @param messageComposer Message Composer
     */
    public AsyncSesEmailSender(SesAsyncClient ses,
                               SesEmailComposer messageComposer) {
        this.ses = ses;
        this.messageComposer = messageComposer;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    /**
     * Sends an email.
     * @param email Email
     * @return {@link software.amazon.awssdk.services.ses.model.SendRawEmailResponse} or {@link software.amazon.awssdk.services.ses.model.SendEmailResponse} or empty optional if an error occurred
     */
    @Override
    @NonNull
    @SingleResult
    public Publisher<SesResponse> sendAsync(@NonNull @NotNull @Valid Email email,
                                            @NonNull @NotNull Consumer<SesRequest> emailRequest) throws EmailException {
        try {
            return sendAsync(messageComposer.compose(email, emailRequest));
        } catch (EmailException e) {
            return Mono.error(e);
        }
    }

    @NonNull
    private Publisher<SesResponse> sendAsync(@NonNull SesRequest sesRequest) {
        if (sesRequest instanceof SendRawEmailRequest) {
            return Mono.fromFuture(ses.sendRawEmail((SendRawEmailRequest) sesRequest));
        } else if (sesRequest instanceof SendEmailRequest) {
            return Mono.fromFuture(ses.sendEmail((SendEmailRequest) sesRequest));
        }
        return Mono.error(new EmailException("SesRequest returned by SesEmailComposer should be either SendRawEmailRequest or SendEmailRequest"));
    }
}
