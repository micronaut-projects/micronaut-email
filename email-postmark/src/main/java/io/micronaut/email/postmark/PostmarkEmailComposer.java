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
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.EmailComposer;
import io.micronaut.email.EmailException;
import io.micronaut.email.TrackLinks;
import jakarta.inject.Singleton;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
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
        final Message message = new Message();
        ifNotNullOrElse(email.getFrom().getName(),
            name -> message.setFrom(name, email.getFrom().getEmail()),
            () -> message.setFrom(email.getFrom().getEmail()));
        ifNotNull(email.getTo(),
            to -> message.setTo(to.stream().map(Contact::getEmail).toList()));
        ifNotNull(email.getCc(),
            cc -> message.setCc(cc.stream().map(Contact::getEmail).toList()));
        ifNotNull(email.getBcc(),
            bcc -> message.setBcc(bcc.stream().map(Contact::getEmail).toList()));
        ifNotNull(email.getReplyToCollection(), validateReplyTo().andThen(
            replyTo -> message.setReplyTo(CollectionUtils.last(replyTo).getEmail())));
        message.setSubject(email.getSubject());
        ifNotNull(email.getBody(), body -> {
            body.get(BodyType.HTML).ifPresent(message::setHtmlBody);
            body.get(BodyType.TEXT).ifPresent(message::setTextBody);
        });
        message.setTrackOpens(postmarkConfiguration.getTrackOpens());
        trackLinks(postmarkConfiguration.getTrackLinks()).ifPresent(message::setTrackLinks);

        ifNotNull(email.getAttachments(), validateAttachments().andThen(
            list -> list.forEach(att -> message.addAttachment(att.getFilename(), att.getContent(), att.getContentType(), att.getId()))));
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

    private static Consumer<Collection<Contact>> validateReplyTo() {
        return replyTo -> {
            if (replyTo.size() > 1 && LOG.isWarnEnabled()) {
                LOG.warn("Postmark does not support multiple 'replyTo' addresses (Email has {} replyTo addresses)", replyTo.size());
            }
        };
    }

    private static Consumer<List<Attachment>> validateAttachments() {
        return attachments -> {
            if (LOG.isWarnEnabled()) {
                attachments.stream()
                    .filter(att -> "inline".equals(att.getDisposition()) && att.getId() == null)
                    .findAny().ifPresent(
                        x -> LOG.warn("Content ID is required for Postmark inlined attachments"));
            }
        };
    }

    private static <T> void ifNotNull(T value, Consumer<T> action) {
        Optional.ofNullable(value).ifPresent(action);
    }

    private static <T> void ifNotNullOrElse(T value, Consumer<T> action, Runnable emptyAction) {
        Optional.ofNullable(value).ifPresentOrElse(action, emptyAction);
    }
}
