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
package io.micronaut.email.sendgrid;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Attachment;
import io.micronaut.email.Body;
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.EmailComposer;
import io.micronaut.email.EmailException;
import jakarta.inject.Singleton;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Composes {@link Request} for {@link io.micronaut.email.Email}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Singleton
public class SendgridEmailComposer implements EmailComposer<Request> {

    private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

    @Override
    @NonNull
    public Request compose(@NonNull @NotNull @Valid Email email) throws EmailException {
        return createRequest(createMail(email));
    }

    @NonNull
    private Mail createMail(@NonNull Email email) {
        Mail mail = new Mail();
        mail.setFrom(createForm(email));
        mail.setSubject(email.getSubject());
        createReplyTo(email).ifPresent(mail::setReplyTo);
        mail.addPersonalization(createPersonalization(email));
        contentOfEmail(email).ifPresent(mail::addContent);

        Map<String, List<String>> headers = email.getCustomHeaders();
        if (null != headers) {
            for (Entry<String, List<String>> entry : headers.entrySet()) {
                for (String value : entry.getValue()) {
                    mail.addHeader(entry.getKey(), value);
                }
            }
        }

        if (email.getAttachments() != null) {
            for (Attachment att : email.getAttachments()) {
                mail.addAttachments(new Attachments.Builder(att.getFilename(), new ByteArrayInputStream(att.getContent()))
                        .withType(att.getContentType())
                        .withContentId(att.getId())
                        .build());
            }
        }
        return mail;
    }

    @NonNull
    private Optional<com.sendgrid.helpers.mail.objects.Email> createReplyTo(@NonNull Email email) {
        if (email.getReplyTo() == null) {
            return Optional.empty();
        }
        com.sendgrid.helpers.mail.objects.Email replyTo = new com.sendgrid.helpers.mail.objects.Email();
        replyTo.setEmail(email.getReplyTo().getEmail());
        if (email.getReplyTo().getName() != null) {
            replyTo.setName(email.getReplyTo().getName());
        }
        return Optional.of(replyTo);
    }

    @NonNull
    private com.sendgrid.helpers.mail.objects.Email createForm(@NonNull Email email) {
        com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email();
        from.setEmail(email.getFrom().getEmail());
        if (email.getFrom().getName() != null) {
            from.setName(email.getFrom().getName());
        }
        return from;
    }

    @NonNull
    private Personalization createPersonalization(@NonNull Email email) {
        Personalization personalization = new Personalization();
        personalization.setSubject(email.getSubject());
        if (email.getTo() != null) {
            for (Contact contactTo : email.getTo()) {
                com.sendgrid.helpers.mail.objects.Email to = new com.sendgrid.helpers.mail.objects.Email();
                to.setEmail(contactTo.getEmail());
                if (contactTo.getName() != null) {
                    to.setName(contactTo.getName());
                }
                personalization.addTo(to);
            }
        }
        if (email.getCc() != null) {
            for (Contact cc : email.getCc()) {
                com.sendgrid.helpers.mail.objects.Email ccEmail = new com.sendgrid.helpers.mail.objects.Email();
                ccEmail.setEmail(cc.getEmail());
                if (cc.getName() != null) {
                    ccEmail.setName(cc.getName());
                }
                personalization.addCc(ccEmail);
            }
        }
        if (email.getBcc()  != null) {
            for (Contact bcc : email.getBcc()) {
                com.sendgrid.helpers.mail.objects.Email bccEmail = new com.sendgrid.helpers.mail.objects.Email();
                bccEmail.setEmail(bcc.getEmail());
                if (bcc.getName() != null) {
                    bccEmail.setName(bcc.getName());
                }
                personalization.addBcc(bccEmail);
            }
        }
        return personalization;
    }

    @NonNull
    private Request createRequest(@NonNull Mail mail) throws EmailException {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            return request;
        } catch (IOException e) {
            throw new EmailException(e);
        }
    }

    @NonNull
    private Optional<Content> contentOfEmail(@NonNull Email email) {
        Body body = email.getBody();
        if (body == null) {
            return Optional.empty();
        }
        Optional<String> str = body.get(BodyType.HTML);
        if (str.isPresent()) {
            return Optional.of(new Content(CONTENT_TYPE_TEXT_HTML, str.get()));
        }
        str = body.get(BodyType.TEXT);
        return str.map(s -> new Content(CONTENT_TYPE_TEXT_PLAIN, s));
    }
}
