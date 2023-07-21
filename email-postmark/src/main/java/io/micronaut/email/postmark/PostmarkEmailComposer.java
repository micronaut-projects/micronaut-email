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

import com.postmarkapp.postmark.client.data.model.message.Message;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.Attachment;
import io.micronaut.email.Body;
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.EmailComposer;
import io.micronaut.email.EmailException;
import io.micronaut.email.TrackLinks;
import jakarta.inject.Singleton;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Composes a {@link Message} given an {@link io.micronaut.email.Email}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Singleton
public class PostmarkEmailComposer implements EmailComposer<Message> {

    private static final Logger LOG = LoggerFactory.getLogger(PostmarkEmailComposer.class);
    private final PostmarkConfiguration postmarkConfiguration;

    /**
     * @param postmarkConfiguration Postmark configuration
     */
    public PostmarkEmailComposer(PostmarkConfiguration postmarkConfiguration) {
        this.postmarkConfiguration = postmarkConfiguration;
    }

    @Override
    @NonNull
    public Message compose(@NonNull @NotNull @Valid Email email) throws EmailException {
        Message message = new Message();
        if (email.getFrom().getName() != null) {
            message.setFrom(email.getFrom().getName(), email.getFrom().getEmail());
        } else {
            message.setFrom(email.getFrom().getEmail());
        }
        if (email.getTo() != null) {
            message.setTo(email.getTo().stream().map(Contact::getEmail).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(email.getReplyToCollection())) {
            if (email.getReplyToCollection().size() > 1 && LOG.isWarnEnabled()) {
                LOG.warn("Postmark does not support multiple 'replyTo' addresses (Email has {} replyTo addresses)", email.getReplyToCollection().size());
            }
            message.setReplyTo(CollectionUtils.last(email.getReplyToCollection()).getEmail());
        }
        message.setSubject(email.getSubject());
        Body body = email.getBody();
        if (body != null) {
            body.get(BodyType.HTML).ifPresent(message::setHtmlBody);
            body.get(BodyType.TEXT).ifPresent(message::setTextBody);
        }
        message.setTrackOpens(postmarkConfiguration.getTrackOpens());
        trackLinks(postmarkConfiguration.getTrackLinks()).ifPresent(message::setTrackLinks);

        if (email.getAttachments() != null) {
            for (Attachment att : email.getAttachments()) {
                message.addAttachment(att.getFilename(), att.getContent(), att.getContentType(), att.getId());
            }
        }
        return message;
    }

    @NonNull
    private Optional<Message.TRACK_LINKS> trackLinks(@NonNull TrackLinks trackLinks) {
        switch (trackLinks) {
            case HTML:
                return Optional.of(Message.TRACK_LINKS.Html);
            case TEXT:
                return Optional.of(Message.TRACK_LINKS.Text);
            case HTML_AND_TEXT:
                return Optional.of(Message.TRACK_LINKS.HtmlAndText);
            case DO_NOT_TRACK:
            default:
                return Optional.empty();
        }
    }
}
