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
package io.micronaut.email;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.email.validation.AnyRecipient;
import io.micronaut.email.validation.Recipients;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Representation of a transactional email.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@AnyRecipient
@Introspected
public final class Email implements Recipients {

    @NonNull
    @NotNull
    @Valid
    private final Contact from;

    @Nullable
    @Valid
    private final Contact replyTo;

    @Nullable
    @Valid
    private final Collection<Contact> to;

    @Nullable
    @Valid
    private final Collection<Contact> cc;

    @Nullable
    @Valid
    private final Collection<Contact> bcc;

    @NotBlank
    @NonNull
    private final String subject;

    @Nullable
    private final List<Attachment> attachments;

    @NonNull
    @NotNull
    private final Body body;

    /**
     *
     * @param from Sender of the Email
     * @param replyTo Reply to
     * @param to To recipients
     * @param cc Carbon Copy recipients
     * @param bcc Blind Carbon Copy recipients
     * @param subject Subject
     * @param attachments Email attachments
     * @param body Email Body
     */
    private Email(@NonNull Contact from,
                 @Nullable Contact replyTo,
                 @Nullable List<Contact> to,
                 @Nullable List<Contact> cc,
                 @Nullable List<Contact> bcc,
                 @NonNull String subject,
                 @Nullable List<Attachment> attachments,
                 @Nullable Body body) {
        this.from = from;
        this.replyTo = replyTo;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.attachments = attachments;
        this.body = body;
    }

    @NonNull
    public Contact getFrom() {
        return from;
    }

    @Nullable
    public Contact getReplyTo() {
        return replyTo;
    }

    @Override
    @Nullable
    public Collection<Contact> getTo() {
        return to;
    }

    @Override
    @Nullable
    public Collection<Contact> getCc() {
        return cc;
    }

    @Override
    @Nullable
    public Collection<Contact> getBcc() {
        return bcc;
    }

    @NonNull
    public String getSubject() {
        return subject;
    }

    @Nullable
    public List<Attachment> getAttachments() {
        return attachments;
    }

    @Nullable
    public Body getBody() {
        return body;
    }

    /**
     *
     * @return Builder
     */
    @NonNull
    public static Email.Builder builder() {
        return new Builder();
    }

    /**
     * Email builder.
     */
    public static class Builder {
        @Nullable
        private Contact from;

        @Nullable
        private List<Contact> to;

        @Nullable
        private String subject;

        @Nullable
        private Contact replyTo;

        @Nullable
        private List<Contact> cc;

        @Nullable
        private List<Contact> bcc;

        @Nullable
        private List<Attachment> attachments;

        @Nullable
        private Body body;

        /**
         *
         * @param from contact sending the email
         * @return Email Builder
         */
        @NonNull
        public Email.Builder from(@NonNull String from) {
            this.from = new Contact(from);
            return this;
        }

        /**
         *
         * @param from contact sending the email
         * @return Email Builder
         */
        @NonNull
        public Email.Builder from(@NonNull Contact from) {
            this.from = from;
            return this;
        }

        /**
         *
         * @param replyTo Reply to contact
         * @return Email Builder
         */
        @NonNull
        public Email.Builder replyTo(@NonNull String replyTo) {
            this.replyTo = new Contact(replyTo);
            return this;
        }

        /**
         *
         * @param replyTo Reply to contact
         * @return Email Builder
         */
        @NonNull
        public Email.Builder replyTo(@NonNull Contact replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        /**
         *
         * @param to Recipients to
         * @return Email Builder
         */
        @NonNull
        public Email.Builder to(@NonNull String to) {
            if (this.to == null) {
                this.to = new ArrayList<>();
            }
            this.to.add(new Contact(to));
            return this;
        }

        /**
         *
         * @param to Recipients to
         * @return Email Builder
         */
        @NonNull
        public Email.Builder to(@NonNull Contact to) {
            addTo(to);
            return this;
        }

        private void addTo(@NonNull Contact to) {
            if (this.to == null) {
                this.to = new ArrayList<>();
            }
            this.to.add(to);
        }

        /**
         *
         * @param cc carbon copy recipient.
         * @return Email Builder
         */
        @NonNull
        public Email.Builder cc(@NonNull Contact cc) {
            addCc(cc);
            return this;
        }

        private void addCc(Contact cc) {
            if (this.cc == null) {
                this.cc = new ArrayList<>();
            }
            this.cc.add(cc);
        }

        /**
         *
         * @param cc carbon copy recipient.
         * @return Email Builder
         */
        @NonNull
        public Email.Builder cc(@NonNull String cc) {
            if (this.cc == null) {
                this.cc = new ArrayList<>();
            }
            this.cc.add(new Contact(cc));
            return this;
        }

        /**
         *
         * @param bcc blind carbon copy recipient.
         * @return Email Builder
         */
        @NonNull
        public Email.Builder bcc(@NonNull Contact bcc) {
            addBcc(bcc);
            return this;
        }

        private void addBcc(@NonNull Contact bcc) {
            if (this.bcc == null) {
                this.bcc = new ArrayList<>();
            }
            this.bcc.add(bcc);
        }

        /**
         *
         * @param bcc blind carbon copy recipient.
         * @return Email Builder
         */
        @NonNull
        public Email.Builder bcc(@NonNull String bcc) {
            if (this.bcc == null) {
                this.bcc = new ArrayList<>();
            }
            this.bcc.add(new Contact(bcc));
            return this;
        }

        /**
         *
         * @param subject Email subject
         * @return Email Builder
         */
        @NonNull
        public Email.Builder subject(@NonNull String subject) {
            this.subject = subject;
            return this;
        }

        /**
         *
         * @param attachment Email attachment
         * @return Email Builder
         */
        @NonNull
        public Email.Builder attachment(@NonNull Attachment attachment) {
            if (this.attachments == null) {
                attachments = new ArrayList<>();
            }
            attachments.add(attachment);
            return this;
        }

        /**
         *
         * @param attachment attachment builder consumer
         * @return Email Builder
         */
        @NonNull
        public Email.Builder attachment(@NonNull Consumer<Attachment.Builder> attachment) {
            Attachment.Builder builder = Attachment.builder();
            attachment.accept(builder);
            return attachment(builder.build());
        }



        /**
         *
         * @param body Email's body
         * @return The Email Builder
         */
        @NonNull
        public Builder body(@NonNull Body body) {
            this.body = body;
            return this;
        }
        
        /**
         * @param body Email body
         * @param bodyType Email body type
         * @return The Email Builder
         */
        @NonNull
        public Builder body(@NonNull String body, BodyType bodyType) {
            this.body = new StringBody(body, bodyType);
            return this;
        }

        /**
         * @param text Email body text
         * @return The Email Builder
         */
        @NonNull
        public Builder body(@NonNull String text) {
            this.body = new StringBody(text);
            return this;
        }

        /**
         * @param html Email body HTML
         * @param text Email body Text
         * @return The Email Builder
         */
        @NonNull
        public Builder body(@NonNull String html, @NonNull String text) {
            this.body = new MultipartBody(new StringBody(html, BodyType.HTML), new StringBody(text, BodyType.TEXT));
            return this;
        }

        /**
         * @return An email
         */
        @NonNull
        public Email build() {
            return new Email(from,
                    replyTo,
                    to,
                    cc,
                    bcc,
                    subject,
                    attachments,
                    body);
        }

        /**
         *
         * @return Email sender
         */
        @NonNull
        public Optional<Contact> getFrom() {
            return Optional.ofNullable(this.from);
        }

        /**
         * @return Email Body
         */
        @NonNull
        public Optional<Body> getBody() {
            return Optional.ofNullable(this.body);
        }

        /**
         *
         * @return Email recipients.
         */
        @NonNull
        public Optional<List<Contact>> getTo() {
            return Optional.ofNullable(to);
        }

        /**
         *
         * @return Email's subject
         */
        @NonNull
        public Optional<String> getSubject() {
            return Optional.ofNullable(subject);
        }

        /**
         *
         * @return Email Reply-to
         */
        @NonNull
        public Optional<Contact> getReplyTo() {
            return Optional.ofNullable(replyTo);
        }

        /**
         *
         * @return Email carbon copy recipients.
         */
        @NonNull
        public Optional<List<Contact>> getCc() {
            return Optional.ofNullable(cc);
        }

        /**
         *
         * @return Email blind carbon copy recipients.
         */
        @NonNull
        public Optional<List<Contact>> getBcc() {
            return Optional.ofNullable(bcc);
        }

        /**
         *
         * @return Email attachments
         */
        @NonNull
        public Optional<List<Attachment>> getAttachments() {
            return Optional.ofNullable(attachments);
        }
    }
}
