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
package io.micronaut.email.postmark;

import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.data.model.message.MessageResponse;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.AbstractTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * <a href="https://postmarkapp.com">Postmark</a> implementation of {@link io.micronaut.email.TransactionalEmailSender} and {@link io.micronaut.email.AsyncTransactionalEmailSender}.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Named(PostmarkEmailSender.NAME)
@Requires(beans = { PostmarkConfiguration.class, PostmarkEmailComposer.class })
@Singleton
public class PostmarkEmailSender extends AbstractTransactionalEmailSender<Message, MessageResponse> {
    /**
     * {@link PostmarkEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "postmark";
    private static final Logger LOG = LoggerFactory.getLogger(PostmarkEmailSender.class);
    private final ApiClient client;
    private final PostmarkEmailComposer postmarkEmailComposer;

    /**
     * @param executorService Executor service
     * @param postmarkConfiguration Postmark configuration
     * @param postmarkEmailComposer Postmark Email Composer
     */
    public PostmarkEmailSender(@Named(TaskExecutors.IO) ExecutorService executorService,
                               PostmarkConfiguration postmarkConfiguration,
                               PostmarkEmailComposer postmarkEmailComposer) {
        super(executorService);
        client = Postmark.getApiClient(postmarkConfiguration.getApiToken());
        this.postmarkEmailComposer = postmarkEmailComposer;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public MessageResponse send(@NonNull @NotNull @Valid Email email,
                                @NonNull @NotNull Consumer<Message> emailRequest) throws EmailException {
        Message message = postmarkEmailComposer.compose(email, emailRequest);
        try {
            MessageResponse response = client.deliverMessage(message);
            if (LOG.isTraceEnabled()) {
                LOG.trace("postmark errorCode: {}", response.getErrorCode() + "");
                LOG.trace("postmark response: {}", response.getMessage());
            }
            return response;
        } catch (PostmarkException | IOException e) {
            throw new EmailException(e);
        }
    }
}
