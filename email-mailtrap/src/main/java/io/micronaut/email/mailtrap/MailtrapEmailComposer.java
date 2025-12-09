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

import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.EmailAttachment;
import io.mailtrap.model.request.emails.MailtrapMail;
import io.micronaut.core.annotation.Internal;
import org.jspecify.annotations.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.Attachment;
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.EmailComposer;
import io.micronaut.email.EmailException;
import jakarta.inject.Singleton;

import java.util.Base64;

/**
 * {@link EmailComposer} implementation for {@link MailtrapMail}.
 */
@Singleton
@Internal
class MailtrapEmailComposer implements EmailComposer<MailtrapMail.MailtrapMailBuilder> {
    @Override
    public @NonNull MailtrapMail.MailtrapMailBuilder compose(@NonNull Email email) throws EmailException {
        var builder = MailtrapMail.builder()
            .subject(email.getSubject())
            .from(address(email.getFrom()));
        if (CollectionUtils.isNotEmpty(email.getTo())) {
            builder.to(email.getTo().stream().map(MailtrapEmailComposer::address).toList());
        }
        if (CollectionUtils.isNotEmpty(email.getCc())) {
            builder.cc(email.getCc().stream().map(MailtrapEmailComposer::address).toList());
        }
        if (CollectionUtils.isNotEmpty(email.getBcc())) {
            builder.bcc(email.getBcc().stream().map(MailtrapEmailComposer::address).toList());
        }
        if (email.getReplyTo() != null) {
            builder.replyTo(address(email.getReplyTo()));
        }
        email.getBody().get(BodyType.TEXT).ifPresent(builder::text);
        email.getBody().get(BodyType.HTML).ifPresent(builder::html);
        if (CollectionUtils.isNotEmpty(email.getAttachments())) {
            builder.attachments(email.getAttachments()
                .stream()
                .map(MailtrapEmailComposer::attachment)
                .toList());
        }
        return builder;
    }

    @NonNull
    private static Address address(@NonNull Contact contact) {
        return new Address(contact.getEmail(), contact.getName());
    }

    @NonNull
    private static EmailAttachment attachment(@NonNull Attachment attachment) {
        return EmailAttachment.builder()
            .content(Base64.getEncoder().encodeToString(attachment.getContent()))
            .contentId(attachment.getId())
            .type(attachment.getContentType())
            .disposition(attachment.getDisposition())
            .filename(attachment.getFilename())
            .build();
    }
}
