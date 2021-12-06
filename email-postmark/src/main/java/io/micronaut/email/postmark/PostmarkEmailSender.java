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
import io.micronaut.email.Attachment;
import io.micronaut.email.Contact;
import io.micronaut.email.EmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.TrackLinks;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@link EmailSender} implementation which uses Postmark.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Named(PostmarkEmailSender.NAME)
@Singleton
public class PostmarkEmailSender implements EmailSender<MessageResponse> {
    /**
     * {@link PostmarkEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "postmark";
    private static final Logger LOG = LoggerFactory.getLogger(PostmarkEmailSender.class);
    private final ApiClient client;

    /**
     *
     * @param postmarkConfiguration Postmark configuration
     */
    public PostmarkEmailSender(PostmarkConfiguration postmarkConfiguration) {
        client = Postmark.getApiClient(postmarkConfiguration.getApiToken());
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    /**
     *
     * @return Whether tracking links is supported.
     */
    @Override
    public boolean isTrackingLinksSupported() {
        return true;
    }

    /**
     *
     * @return Whether sending attachments is supported
     */
    @Override
    public boolean isSendingAttachmentsSupported() {
        return true;
    }

    @Override
    @NonNull
    public Optional<MessageResponse> send(@NonNull @NotNull @Valid Email email) {
        Message message = new Message();
        if (email.getFrom().getName() != null) {
            message.setFrom(email.getFrom().getName(), email.getFrom().getEmail());
        } else {
            message.setFrom(email.getFrom().getEmail());
        }
        if (email.getTo() != null) {
            message.setTo(email.getTo().stream().map(Contact::getEmail).collect(Collectors.toList()));
        }
        message.setSubject(email.getSubject());
        if (email.getText() != null) {
            message.setTextBody(email.getText());
        }
        if (email.getHtml() != null) {
            message.setHtmlBody(email.getHtml());
        }
        if (email.getTrackOpens()) {
            message.setTrackOpens(email.getTrackOpens());
        }
        if (email.getTrackLinks() != null) {
            message.setTrackLinks(trackLinks(email.getTrackLinks()));
        }
        if (email.getAttachments() != null) {
            for (Attachment att : email.getAttachments()) {
                message.addAttachment(att.getFilename(), att.getContent(), att.getContentType(), att.getId());
            }
        }
        try {
            MessageResponse response = client.deliverMessage(message);
            if (LOG.isTraceEnabled()) {
                LOG.trace("postmark errorCode: {}", response.getErrorCode() + "");
                LOG.trace("postmark response: {}", response.getMessage());
            }
            return Optional.of(response);
        } catch (PostmarkException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Postmark exception", e);
            }
        } catch (IOException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("IO Exception", e);
            }
        }
        return Optional.empty();
    }

    @NonNull
    private Message.TRACK_LINKS trackLinks(@NonNull TrackLinks trackLinks) {
        switch (trackLinks) {
            case HTML:
                return Message.TRACK_LINKS.Html;
            case TEXT:
                return Message.TRACK_LINKS.Text;
            case HTML_AND_TEXT:
            default:
                return Message.TRACK_LINKS.HtmlAndText;
        }
    }
}
