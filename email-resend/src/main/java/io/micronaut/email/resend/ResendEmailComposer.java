/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.email.resend;

import io.micronaut.core.annotation.Internal;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.email.Attachment;
import io.micronaut.email.Body;
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.EmailComposer;
import io.micronaut.email.EmailException;
import io.micronaut.email.resend.model.ResendAttachment;
import io.micronaut.email.resend.model.ResendEmailRequest;
import jakarta.inject.Singleton;
import org.jspecify.annotations.Nullable;

import java.util.Base64;
import java.util.Collection;
import java.util.List;

/**
 * {@link EmailComposer} implementation for {@link ResendEmailRequest}.
 */
@Singleton
@Internal
class ResendEmailComposer implements EmailComposer<ResendEmailRequest.Builder> {

    @Override
    public ResendEmailRequest.Builder compose(Email email) throws EmailException {
        ResendEmailRequest.Builder builder = ResendEmailRequest.builder()
            .from(address(email.getFrom()))
            .subject(email.getSubject())
            .to(addresses(email.getTo()))
            .cc(addresses(email.getCc()))
            .bcc(addresses(email.getBcc()))
            .replyTo(addresses(email.getReplyToCollection()));
        Body body = email.getBody();
        if (body != null) {
            body.get(BodyType.TEXT).ifPresent(builder::text);
            body.get(BodyType.HTML).ifPresent(builder::html);
        }
        if (CollectionUtils.isNotEmpty(email.getAttachments())) {
            builder.attachments(email.getAttachments()
                .stream()
                .map(ResendEmailComposer::attachment)
                .toList());
        }
        return builder;
    }

    private static @Nullable List<String> addresses(@Nullable Collection<Contact> contacts) {
        return CollectionUtils.isEmpty(contacts) ? null : contacts.stream()
            .map(ResendEmailComposer::address)
            .toList();
    }

    private static String address(Contact contact) {
        return StringUtils.isNotEmpty(contact.getName()) ? contact.getNameAddress() : contact.getEmail();
    }

    private static ResendAttachment attachment(Attachment attachment) {
        return new ResendAttachment(
            Base64.getEncoder().encodeToString(attachment.getContent()),
            attachment.getFilename(),
            null,
            attachment.getContentType(),
            attachment.getId()
        );
    }
}
