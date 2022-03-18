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
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.Attachment;
import io.micronaut.email.Body;
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static final EnumMap<BodyType, String> BODY_TYPES;

    static {
        EnumMap<BodyType, String> m = new EnumMap<>(BodyType.class);
        m.put(BodyType.TEXT, TYPE_TEXT_PLAIN_CHARSET_UTF_8);
        m.put(BodyType.HTML, TYPE_TEXT_HTML_CHARSET_UTF_8);
        BODY_TYPES = m;
    }

    @Override
    @NonNull
    public Message compose(@NonNull Email email,
                           @NonNull Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setSubject(email.getSubject(), "UTF-8");
        message.setFrom(new InternetAddress(email.getFrom().getEmail()));
        if (CollectionUtils.isNotEmpty(email.getTo())) {
            message.setRecipients(Message.RecipientType.TO, contactAddresses(email.getTo()));
        }
        if (CollectionUtils.isNotEmpty(email.getCc())) {
            message.setRecipients(Message.RecipientType.CC, contactAddresses(email.getCc()));
        }
        if (CollectionUtils.isNotEmpty(email.getBcc())) {
            message.setRecipients(Message.RecipientType.BCC, contactAddresses(email.getBcc()));
        }
        if (null != email.getReplyTo()) {
            message.setReplyTo(contactAddresses(Stream.of(email.getReplyTo()).collect(Collectors.toList())));
        }

        MimeMultipart multipart = new MimeMultipart();

        Body body = email.getBody();
        if (body != null) {
            multipart.addBodyPart(bodyPart(body));
        }
        for (MimeBodyPart bodyPart : attachmentBodyParts(email)) {
            multipart.addBodyPart(bodyPart);
        }
        message.setContent(multipart);
        return message;
    }

    @NonNull
    private MimeBodyPart bodyPart(@NonNull Body body) throws MessagingException {
        final MimeBodyPart mbp = new MimeBodyPart();
        MimeMultipart alternativeBody = new MimeMultipart(SUBTYPE_ALTERNATIVE);
        mbp.setContent(alternativeBody);
        for (MimeBodyPart part : bodyParts(body)) {
            alternativeBody.addBodyPart(part);
        }
        return mbp;
    }

    @NonNull
    private Address[] contactAddresses(@NonNull Collection<Contact> contacts) throws AddressException {
        List<Address> addressList = new ArrayList<>();
        for (Contact contact : contacts) {
            addressList.addAll(Arrays.asList(InternetAddress.parse(contact.getEmail())));
        }
        Address[] array = new Address[addressList.size()];
        addressList.toArray(array);
        return array;
    }

    @NonNull
    private static List<MimeBodyPart> bodyParts(@NonNull Body body) {
        List<MimeBodyPart> result = new ArrayList<>();
        for (Map.Entry<BodyType, String> entry : BODY_TYPES.entrySet()) {
            body.get(entry.getKey()).ifPresent(it -> {
                try {
                    result.add(partForContent(entry.getValue(), it));
                } catch (MessagingException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Messaging exception setting {} body part", entry.getValue(), e);
                    }
                }
            });
        }
        return result;
    }

    @NonNull
    private static MimeBodyPart partForContent(@NonNull String type, @NonNull String content) throws MessagingException {
        MimeBodyPart part = new MimeBodyPart();
        part.setContent(content, type);
        return part;
    }

    @NonNull
    private List<MimeBodyPart> attachmentBodyParts(@NonNull Email email) throws MessagingException {
        if (email.getAttachments() == null) {
            return Collections.emptyList();
        }
        List<MimeBodyPart> list = new ArrayList<>();
        for (Attachment attachment : email.getAttachments()) {
            MimeBodyPart mimeBodyPart = attachmentBodyPart(attachment);
            list.add(mimeBodyPart);
        }
        return list;
    }

    private MimeBodyPart attachmentBodyPart(@NonNull Attachment attachment) throws MessagingException {
        MimeBodyPart att = new MimeBodyPart();
        DataSource fds = new ByteArrayDataSource(attachment.getContent(), attachment.getContentType());
        att.setDataHandler(new DataHandler(fds));
        String reportName = attachment.getFilename();
        att.setFileName(reportName);
        return att;
    }
}
