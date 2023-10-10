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
package io.micronaut.email.ses;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.EmailComposer;
import io.micronaut.email.EmailException;
import io.micronaut.email.javamail.composer.MessageComposer;
import jakarta.inject.Singleton;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;
import software.amazon.awssdk.services.ses.model.SesRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;

/**
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Singleton
public class SesEmailComposer implements EmailComposer<SesRequest> {
    private final MessageComposer messageComposer;

    /**
     * @param messageComposer Message Composer
     */
    public SesEmailComposer(MessageComposer messageComposer) {
        this.messageComposer = messageComposer;
    }

    @NonNull
    @Override
    public SesRequest compose(@NonNull @NotNull @Valid Email email) throws EmailException {
        if (CollectionUtils.isEmpty(email.getAttachments())) {
            return sendEmailRequest(email);
        }
        try {
            return sendRawEmailRequest(email);
        } catch (MessagingException | IOException e) {
            throw new EmailException(e);
        }
    }

    @NonNull
    private SendRawEmailRequest sendRawEmailRequest(@NonNull Email email) throws MessagingException, IOException {
        return SendRawEmailRequest.builder()
                .rawMessage(RawMessage.builder()
                        .data(bytesOfMessage(messageComposer.compose(email, Session.getDefaultInstance(new Properties()))))
                        .build())
                .build();
    }

    @NonNull
    private SdkBytes bytesOfMessage(@NonNull Message message) throws IOException, MessagingException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);
        ByteBuffer buf = ByteBuffer.wrap(outputStream.toByteArray());
        byte[] arr = new byte[buf.remaining()];
        buf.get(arr);
        return SdkBytes.fromByteArray(arr);
    }

    @NonNull
    private SendEmailRequest sendEmailRequest(@NonNull Email email) {
        SendEmailRequest.Builder requestBuilder = SendEmailRequest.builder()
                .destination(destinationBuilder(email).build())
                .source(StringUtils.isNotEmpty(email.getFrom().getName()) ?
                    email.getFrom().getNameAddress() :
                    email.getFrom().getEmail())
                .message(message(email));
        if (CollectionUtils.isNotEmpty(email.getReplyToCollection())) {
            requestBuilder = requestBuilder.replyToAddresses(
                email.getReplyToCollection().stream().map(Contact::getEmail).toList());
        }
        return requestBuilder.build();
    }

    @NonNull
    private Destination.Builder destinationBuilder(@NonNull Email email) {
        Destination.Builder destinationBuilder = Destination.builder();
        if (email.getTo() != null) {
            destinationBuilder.toAddresses(email.getTo().stream().map(Contact::getEmail).toList());
        }
        if (email.getCc() != null) {
            destinationBuilder.ccAddresses(email.getCc().stream().map(Contact::getEmail).toList());
        }
        if (email.getBcc() != null) {
            destinationBuilder.bccAddresses(email.getBcc().stream().map(Contact::getEmail).toList());
        }
        return destinationBuilder;
    }

    @NonNull
    private software.amazon.awssdk.services.ses.model.Message message(@NonNull Email email) {
        return software.amazon.awssdk.services.ses.model.Message.builder()
                .subject(Content.builder().data(email.getSubject()).build())
                .body(bodyBuilder(email).build())
                .build();
    }

    @NonNull
    private Body.Builder bodyBuilder(@NonNull Email email) {
        Body.Builder bodyBuilder = Body.builder();
        io.micronaut.email.Body body = email.getBody();
        if (body != null) {
            body.get(BodyType.TEXT).map(it -> Content.builder().data(it).build()).ifPresent(bodyBuilder::text);
            body.get(BodyType.HTML).map(it -> Content.builder().data(it).build()).ifPresent(bodyBuilder::html);
        }
        return bodyBuilder;
    }
}
