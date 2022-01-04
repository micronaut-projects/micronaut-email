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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.TransactionalEmailSender;
import io.micronaut.email.javaxemail.MessageComposer;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;
import software.amazon.awssdk.services.ses.model.SendRawEmailResponse;
import software.amazon.awssdk.services.ses.model.SesResponse;

import javax.mail.Session;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * <a href="https://aws.amazon.com/es/ses/">Amazon Simple Email Service</a> implementation of {@link TransactionalEmailSender}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Named(SesEmailSender.NAME)
@Requires(beans = SesClient.class)
@Singleton
public class SesEmailSender implements TransactionalEmailSender<SesResponse> {
    /**
     * {@link SesEmailSender} name.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String NAME = "ses";

    private static final Logger LOG = LoggerFactory.getLogger(SesEmailSender.class);

    private final SesClient ses;
    private final MessageComposer messageComposer;

    /**
     *
     * @param ses Amazon Simple Email Service Client
     * @param messageComposer Message Composer
     */
    public SesEmailSender(SesClient ses, MessageComposer messageComposer) {
        this.ses = ses;
        this.messageComposer = messageComposer;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    /**
     * Sends an email.
     * @param email Email
     * @return {@link SendRawEmailResponse} or {@link SendEmailResponse} or empty optional if an error occurred
     */
    @NonNull
    public Optional<SesResponse> send(@NonNull @NotNull @Valid Email email) {
        if (CollectionUtils.isEmpty(email.getAttachments())) {
            SendEmailResponse response = ses.sendEmail(sendEmailRequest(email));
            if (LOG.isTraceEnabled()) {
                LOG.trace("Send Email response: {}", response.toString());
            }
            return Optional.of(response);
        }
        return sendRawEmail(email);
    }

    @NonNull
    private Optional<SesResponse> sendRawEmail(@NonNull Email email) {
        try {
            SendRawEmailResponse rawEmailResponse = ses.sendRawEmail(sendRawEmailRequest(email));
            if (LOG.isTraceEnabled()) {
                LOG.trace("Send Raw Email response: {}", rawEmailResponse.toString());
            }
            return Optional.of(rawEmailResponse);
        } catch (MessagingException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Messaging Exception sending email with attachments", e);
            }
        } catch (IOException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("IOException sending email with attachments", e);
            }
        }
        return Optional.empty();
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
                .source(email.getFrom().getEmail())
                .message(message(email));
        if (email.getReplyTo() != null) {
            requestBuilder = requestBuilder.replyToAddresses(email.getReplyTo().getEmail());
        }
        return requestBuilder.build();
    }

    @NonNull
    private Destination.Builder destinationBuilder(@NonNull Email email) {
        Destination.Builder destinationBuilder = Destination.builder();
        if (email.getTo() != null) {
            destinationBuilder.toAddresses(email.getTo().stream().map(Contact::getEmail).collect(Collectors.toList()));
        }
        if (email.getCc() != null) {
            destinationBuilder.toAddresses(email.getCc().stream().map(Contact::getEmail).collect(Collectors.toList()));
        }
        if (email.getBcc() != null) {
            destinationBuilder.toAddresses(email.getBcc().stream().map(Contact::getEmail).collect(Collectors.toList()));
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
        if (email.getHtml() != null) {
            bodyBuilder.html(Content.builder().data(email.getHtml()).build());
        }
        if (email.getText() != null) {
            bodyBuilder.text(Content.builder().data(email.getText()).build());
        }
        return bodyBuilder;
    }

}
