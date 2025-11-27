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
package io.micronaut.email.javamail.composer;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.email.Attachment;
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.inject.Singleton;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * {@link io.micronaut.context.annotation.DefaultImplementation} of {@link MessageComposer}.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Singleton
public class DefaultMessageComposer implements MessageComposer {
    public static final String TYPE_TEXT_PLAIN_CHARSET_UTF_8 = "text/plain; charset=UTF-8";
    public static final String TYPE_TEXT_HTML_CHARSET_UTF_8 = "text/html; charset=UTF-8";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMessageComposer.class);
    private static final String SUBTYPE_ALTERNATIVE = "alternative";
    private static final String SUBTYPE_RELATED = "related";

    @Override
    @NonNull
    public Message compose(@NonNull Email email,
                           @NonNull Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setSubject(email.getSubject(), "UTF-8");
        message.setFrom(contactToAddress(email.getFrom()));
        if (CollectionUtils.isNotEmpty(email.getTo())) {
            message.setRecipients(Message.RecipientType.TO, contactAddresses(email.getTo()));
        }
        if (CollectionUtils.isNotEmpty(email.getCc())) {
            message.setRecipients(Message.RecipientType.CC, contactAddresses(email.getCc()));
        }
        if (CollectionUtils.isNotEmpty(email.getBcc())) {
            message.setRecipients(Message.RecipientType.BCC, contactAddresses(email.getBcc()));
        }
        if (CollectionUtils.isNotEmpty(email.getReplyToCollection())) {
            message.setReplyTo(contactAddresses(email.getReplyToCollection()));
        }
        try {
            Optional<Multipart> multipartOptional = content(email);
            if (multipartOptional.isPresent()) {
                Multipart multipart = multipartOptional.get();
                message.setContent(multipart);
            }
        } catch (MessagingException e) {
            LOG.warn("MessagingException setting email content", e);
        }
        return message;
    }

    @NonNull
    private static Optional<Multipart> content(@NonNull Email email) throws MessagingException {
        List<Attachment> attachments = Optional.ofNullable(email.getAttachments()).orElse(Collections.emptyList());
        List<Attachment> inlineAttachments = attachments.stream()
            .filter(a -> a.getDisposition() != null && a.getDisposition().equals(Part.INLINE))
            .toList();
        List<Attachment> regularAttachments = attachments.stream()
            .filter(a -> a.getDisposition() == null || !a.getDisposition().equals(Part.INLINE))
            .toList();

        Optional<String> htmlOptional = email.getBody().get(BodyType.HTML);
        Optional<String> textOptional = email.getBody().get(BodyType.TEXT);

        if (CollectionUtils.isEmpty(inlineAttachments) && CollectionUtils.isEmpty(regularAttachments)) {
            if (htmlOptional.isPresent() && textOptional.isPresent()) {
                LOG.trace("Email has both HTML and text body");
                MimeMultipart alternative = alternativeMimeMultipart(textOptional.get(), htmlOptional.get());
                return Optional.of(alternative);
            } else if (htmlOptional.isPresent()) {
                LOG.trace("Email has only HTML body");
                MimeMultipart multipart = new MimeMultipart();
                multipart.addBodyPart(createHtmlPart(htmlOptional.get()));
                return Optional.of(multipart);
            } else if (textOptional.isPresent()) {
                LOG.trace("Email has only text body");
                MimeMultipart multipart = new MimeMultipart();
                multipart.addBodyPart(createTextPart(textOptional.get()));
                return Optional.of(multipart);
            }
            LOG.trace("Email does not have HTML or text body");
            return Optional.empty();
        }

        MimeMultipart mixed = new MimeMultipart();
        if (CollectionUtils.isNotEmpty(inlineAttachments)) {
            MimeMultipart related = new MimeMultipart(SUBTYPE_RELATED);
            Optional<MimeBodyPart> mimeBodyPartOptional = mimeBodyPart(textOptional.orElse(null), htmlOptional.orElse(null));
            if (mimeBodyPartOptional.isPresent()) {
                related.addBodyPart(mimeBodyPartOptional.get());
            }
            for (Attachment attachment : inlineAttachments) {
                related.addBodyPart(createAttachmentPart(attachment));
            }
            MimeBodyPart relatedWrapper = new MimeBodyPart();
            relatedWrapper.setContent(related);
            mixed.addBodyPart(relatedWrapper);
        } else {
            Optional<MimeBodyPart> mimeBodyPartOptional = mimeBodyPart(textOptional.orElse(null), htmlOptional.orElse(null));
            if (mimeBodyPartOptional.isPresent()) {
                mixed.addBodyPart(mimeBodyPartOptional.get());
            }
        }
        for (Attachment attachment : regularAttachments) {
            mixed.addBodyPart(createAttachmentPart(attachment));
        }
        return Optional.of(mixed);
    }

    @NonNull
    private static Optional<MimeBodyPart> mimeBodyPart(@Nullable String text, @Nullable String html) throws MessagingException {
        if (StringUtils.isNotEmpty(text) && StringUtils.isNotEmpty(html)) {
            LOG.trace("Email has both HTML and text body");
            return Optional.of(alternativeWrapper(text, html));
        } else if (StringUtils.isNotEmpty(html)) {
            LOG.trace("Email has only HTML body");
            return Optional.of(createHtmlPart(html));
        } else if (StringUtils.isNotEmpty(text)) {
            LOG.trace("Email has only text body");
            return Optional.of(createTextPart(text));
        }
        return Optional.empty();
    }

    @NonNull
    private static MimeMultipart alternativeMimeMultipart(@NonNull String text, @NonNull String html) throws MessagingException {
        MimeMultipart alternative = new MimeMultipart(SUBTYPE_ALTERNATIVE);
        alternative.addBodyPart(createTextPart(text));
        alternative.addBodyPart(createHtmlPart(html));
        return alternative;
    }

    @NonNull
    private static MimeBodyPart alternativeWrapper(@NonNull String text, @NonNull String html) throws MessagingException {
        MimeMultipart alternative = alternativeMimeMultipart(text, html);
        MimeBodyPart alternativeWrapper = new MimeBodyPart();
        alternativeWrapper.setContent(alternative);
        return alternativeWrapper;
    }

    private static MimeBodyPart createTextPart(String text) throws MessagingException {
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(text, TYPE_TEXT_PLAIN_CHARSET_UTF_8);
        return textPart;
    }

    private static MimeBodyPart createHtmlPart(String html) throws MessagingException {
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(html, TYPE_TEXT_HTML_CHARSET_UTF_8);
        return htmlPart;
    }

    private static MimeBodyPart createAttachmentPart(Attachment attachment) throws MessagingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(attachment.getContent(), attachment.getContentType());
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(attachment.getFilename());
        attachmentPart.setHeader("Content-Type", attachment.getContentType());
        if (attachment.getDisposition() != null) {
            attachmentPart.setDisposition(attachment.getDisposition());
        } else {
            attachmentPart.setDisposition(Part.ATTACHMENT);
        }
        if (attachment.getId() != null) {
            attachmentPart.setContentID("<" + attachment.getId() + ">");
        }
        return attachmentPart;
    }

    @NonNull
    private Address[] contactAddresses(@NonNull Collection<Contact> contacts) throws MessagingException {
        List<Address> addressList = new ArrayList<>();
        for (Contact contact : contacts) {
            addressList.add(contactToAddress(contact));
        }
        Address[] array = new Address[addressList.size()];
        addressList.toArray(array);
        return array;
    }

    private InternetAddress contactToAddress(Contact contact) throws MessagingException {
        if (StringUtils.isNotEmpty(contact.getName())) {
            try {
                return new InternetAddress(contact.getEmail(), contact.getName(), StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                // This should never occur since UTF-8 is a supported encoding.
                throw new MessagingException(e.getLocalizedMessage(),  e);
            }
        } else {
            return new InternetAddress(contact.getEmail());
        }
    }
}
