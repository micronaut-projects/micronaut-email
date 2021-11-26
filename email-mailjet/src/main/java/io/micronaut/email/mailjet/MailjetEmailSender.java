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
import com.mailjet.client.resource.Emailv31;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.EmailSender;
import io.micronaut.email.Email;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * <a href="https://www.mailjet.com">Mailjet</a> implementation of {@link EmailSender}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Named(MailjetEmailSender.NAME)
@Singleton
public class MailjetEmailSender implements EmailSender {
    /**
     * {@link MailjetEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "mailjet";

    private static final Logger LOG = LoggerFactory.getLogger(MailjetEmailSender.class);

    private final MailjetClient mailjetClient;

    /**
     *
     * @param mailjetConfiguration Mailjet Configuration.
     */
    public MailjetEmailSender(MailjetConfiguration mailjetConfiguration) {
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
    public void send(@NonNull @NotNull @Valid Email email) {
        MailjetRequest request = createRequest(email);
        try {
            MailjetResponse response = mailjetClient.post(request);
            if (LOG.isTraceEnabled()) {
                LOG.trace("response status: {}", response.getStatus());
                LOG.trace("response data: {}", response.getData().toString());
            }
        } catch (MailjetException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Mailjet exception", e);
            }
        }
    }

    @NonNull
    private MailjetRequest createRequest(@NonNull Email email) {
        JSONObject message = new JSONObject();
        message.put(Emailv31.Message.FROM, from(email));
        to(email).ifPresent(jsonArray -> message.put(Emailv31.Message.TO, jsonArray));
        message.put(Emailv31.Message.SUBJECT, email.getSubject());
        if (email.getText() != null) {
            message.put(Emailv31.Message.TEXTPART, email.getText());
        }
        if (email.getHtml() != null) {
            message.put(Emailv31.Message.HTMLPART, email.getHtml());
        }
        JSONArray messages = new JSONArray();
        messages.put(message);
        return new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, messages);
    }

    @NonNull
    private static JSONObject from(@NonNull Email email) {
        return new JSONObject().put("Email", email.getFrom().getEmail());
    }

    @NonNull
    private static Optional<JSONArray> to(@NonNull Email email) {
        if (email.getTo().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new JSONArray().put(new JSONObject().put("Email", email.getTo().get(0).getEmail())));
    }
}
