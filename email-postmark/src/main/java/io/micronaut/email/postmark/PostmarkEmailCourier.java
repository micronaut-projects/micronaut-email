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

import com.wildbit.java.postmark.Postmark;
import com.wildbit.java.postmark.client.ApiClient;
import com.wildbit.java.postmark.client.data.model.message.Message;
import com.wildbit.java.postmark.client.data.model.message.MessageResponse;
import com.wildbit.java.postmark.client.exception.PostmarkException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.EmailCourier;
import io.micronaut.email.TransactionalEmail;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * {@link EmailCourier} implementation which uses Postmark.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Named("postmark")
@Singleton
public class PostmarkEmailCourier implements EmailCourier {
    private static final Logger LOG = LoggerFactory.getLogger(PostmarkEmailCourier.class);

    private final ApiClient client;

    /**
     *
     * @param postmarkConfiguration Postmark configuration
     */
    public PostmarkEmailCourier(PostmarkConfiguration postmarkConfiguration) {
        client = Postmark.getApiClient(postmarkConfiguration.getApiToken());
    }

    @Override
    public void send(@NonNull @NotNull @Valid TransactionalEmail email) {
        Message message = new Message(email.getFrom().getEmail(),
                email.getTo().isEmpty() ? null : email.getTo().get(0).getEmail(),
                email.getSubject(), email.getText(), email.getHtml());
        try {
            MessageResponse response = client.deliverMessage(message);
            if (LOG.isTraceEnabled()) {
                LOG.trace("postmark errorCode: {}", response.getErrorCode() + "");
                LOG.trace("postmark response: {}", response.getMessage());
            }
        } catch (PostmarkException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Postmark exception", e);
            }
        } catch (IOException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("IO Exception", e);
            }
        }
    }
}
