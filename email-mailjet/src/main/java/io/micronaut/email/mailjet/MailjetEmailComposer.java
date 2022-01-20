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
package io.micronaut.email.mailjet;

import com.mailjet.client.MailjetRequest;
import com.mailjet.client.resource.Emailv31;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.Attachment;
import io.micronaut.email.Body;
import io.micronaut.email.BodyType;
import io.micronaut.email.Contact;
import io.micronaut.email.Email;
import io.micronaut.email.EmailComposer;
import io.micronaut.email.EmailException;
import jakarta.inject.Singleton;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.util.Optional;

/**
 * Composes a {@link MailjetRequest} given an {@link io.micronaut.email.Email}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Singleton
public class MailjetEmailComposer implements EmailComposer<MailjetRequest> {
    @Override
    @NonNull
    public MailjetRequest compose(@NonNull @NotNull @Valid Email email) throws EmailException {
        JSONObject message = new JSONObject();
        message.put(Emailv31.Message.FROM, createJsonObject(email.getFrom()));
        to(email).ifPresent(jsonArray -> message.put(Emailv31.Message.TO, jsonArray));
        message.put(Emailv31.Message.SUBJECT, email.getSubject());
        Body body = email.getBody();
        if (body != null) {
            if (body.getType() == BodyType.HTML) {
                message.put(Emailv31.Message.HTMLPART, body.get());
            } else if (body.getType() == BodyType.TEXT) {
                message.put(Emailv31.Message.TEXTPART, body.get());
            }
        }
        attachmentsAsJsonArray(email).ifPresent(arr -> message.put(Emailv31.Message.ATTACHMENTS, arr));
        JSONArray messages = new JSONArray();
        messages.put(message);
        return new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, messages);
    }

    @NonNull
    private static Optional<JSONArray> attachmentsAsJsonArray(@NonNull Email email) {
        if (CollectionUtils.isEmpty(email.getAttachments())) {
            return Optional.empty();
        }
        JSONArray arr = new JSONArray();
        email.getAttachments().forEach(att -> arr.put(attachmentAsJsonObject(att)));
        return Optional.of(arr);
    }

    @NonNull
    private static JSONObject attachmentAsJsonObject(@NonNull Attachment attachment) {
        return new JSONObject().put("ContentType", attachment.getContentType())
                .put("Filename", attachment.getFilename())
                .put("Base64Content", Base64.getEncoder().encodeToString(attachment.getContent()));
    }

    @NonNull
    private static Optional<JSONArray> to(@NonNull Email email) {
        if (CollectionUtils.isEmpty(email.getTo())) {
            return Optional.empty();
        }
        JSONArray arr = new JSONArray();
        for (Contact to : email.getTo()) {
            arr.put(createJsonObject(to));
        }
        return Optional.of(arr);
    }

    @NonNull
    private static JSONObject createJsonObject(@NonNull Contact contact) {
        return new JSONObject().put("Email", contact.getEmail());
    }
}
