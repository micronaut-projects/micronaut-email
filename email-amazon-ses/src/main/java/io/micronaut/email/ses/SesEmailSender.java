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
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.TransactionalEmailSender;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;
import software.amazon.awssdk.services.ses.model.SendRawEmailResponse;
import software.amazon.awssdk.services.ses.model.SesRequest;
import software.amazon.awssdk.services.ses.model.SesResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

/**
 * <a href="https://aws.amazon.com/es/ses/">Amazon Simple Email Service</a> implementation of {@link io.micronaut.email.TransactionalEmailSender}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Named(SesEmailSender.NAME)
@Requires(beans = SesClient.class)
@Singleton
public class SesEmailSender implements TransactionalEmailSender<SesRequest, SesResponse> {
    /**
     * {@link SesEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "ses";

    private static final Logger LOG = LoggerFactory.getLogger(SesEmailSender.class);

    private final SesClient ses;
    private final SesEmailComposer messageComposer;

    /**
     * @param ses Amazon Simple Email Service Client
     * @param messageComposer Message Composer
     */
    public SesEmailSender(SesClient ses,
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
     * @return {@link SendRawEmailResponse} or {@link SendEmailResponse} or empty optional if an error occurred
     */
    @Override
    @NonNull
    public SesResponse send(@NonNull @NotNull @Valid Email email,
                            @NonNull @NotNull Consumer<SesRequest> emailRequest) throws EmailException {
        SesRequest sesRequest = messageComposer.compose(email, emailRequest);
        if (sesRequest instanceof SendRawEmailRequest) {
            return sendRawEmail((SendRawEmailRequest) sesRequest);
        } else if (sesRequest instanceof SendEmailRequest) {
            return sendEmailRequest((SendEmailRequest) sesRequest);
        }
        throw new EmailException("SesRequest returned by SesEmailComposer should be either SendRawEmailRequest or SendEmailRequest");
    }

    @NonNull
    private SesResponse sendEmailRequest(@NonNull SendEmailRequest email) throws EmailException {
        SendEmailResponse response = ses.sendEmail(email);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Send Email response: {}", response.toString());
        }
        return response;
    }

    @NonNull
    private SesResponse sendRawEmail(@NonNull SendRawEmailRequest email) throws EmailException {
        SendRawEmailResponse rawEmailResponse = ses.sendRawEmail(email);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Send Raw Email response: {}", rawEmailResponse.toString());
        }
        return rawEmailResponse;
    }
}
