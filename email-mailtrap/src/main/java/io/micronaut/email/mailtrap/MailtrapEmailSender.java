/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.email.mailtrap;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.model.request.emails.MailtrapMail;
import io.mailtrap.model.response.emails.SendResponse;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Internal;
import org.jspecify.annotations.NonNull;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.TransactionalEmailSender;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

@Requires(beans = { MailtrapClient.class, MailtrapEmailComposer.class })
@Named(MailtrapEmailSender.NAME)
@Singleton
@Internal
class MailtrapEmailSender implements TransactionalEmailSender<MailtrapMail.MailtrapMailBuilder, SendResponse> {
    /**
     * {@link MailtrapEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "mailtrap";
    private static final Logger LOG = LoggerFactory.getLogger(MailtrapEmailSender.class);
    private final MailtrapClient client;
    private final MailtrapEmailComposer composer;

    MailtrapEmailSender(@NonNull MailtrapClient client,
                        @NonNull MailtrapEmailComposer composer) {
        this.client = client;
        this.composer = composer;
    }

    @Override
    public SendResponse send(Email email, Consumer<MailtrapMail.MailtrapMailBuilder> emailRequest) throws EmailException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Sending email to {}", email.getTo());
        }
        SendResponse response = client.send(composer.compose(email, emailRequest).build());
        if (LOG.isTraceEnabled()) {
            LOG.trace("Is Success: {}", response.isSuccess());
            LOG.trace("Message Ids: {}", response.getMessageIds());
        }
        return response;
    }

    @Override
    public @NonNull String getName() {
        return NAME;
    }
}
