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
import io.micronaut.core.util.StringUtils;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representation of a transactional email.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Introspected
public class Email {

    @NonNull
    @NotNull
    @Valid
    private final EmailHeader emailHeader;

    @Nullable
    private final String html;

    @Nullable
    private final String text;

    @Nullable
    private List<Attachment> attachments;

    /**
     *
     * @param emailHeader Email Sender, recipients and subjects
     * @param html Email content as HTML
     * @param text Email content as text
     * @param attachments Email attachments
     */
    public Email(@NonNull EmailHeader emailHeader,
                 @Nullable String html,
                 @Nullable String text,
                 @Nullable List<Attachment> attachments) {
        this.emailHeader = emailHeader;
        this.html = html;
        this.text = text;
        this.attachments = attachments;
    }

    /**
     *
     * @return Email attachments
     */
    @Nullable
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @param from Sender of the Email
     * @param replyTo Reply to
     * @param to To recipients
     * @param cc Carbon Copy recipients
     * @param bcc Blind Carbon Copy recipients
     * @param subject Subject
     * @param html Email content as HTML
     * @param text Email content as text
     * @param trackOpens Whether to track if the email is opened
     * @param trackLinks Whether email links should be tracked
     */
    public Email(@NonNull Contact from,
                 @Nullable Contact replyTo,
                 @Nullable List<Contact> to,
                 @Nullable List<Contact> cc,
                 @Nullable List<Contact> bcc,
                 @NonNull String subject,
                 @Nullable String html,
                 @Nullable String text,
                 boolean trackOpens,
                 @Nullable TrackLinks trackLinks) {
        this.emailHeader = new EmailHeader(from, replyTo, to, cc, bcc, subject, trackOpens, trackLinks);
        this.html = html;
        this.text = text;
    }

    /**
     *
     * @return Email carbon copy recipients.
     */
    @Nullable
    public List<Contact> getCc() {
        return emailHeader.getCc();
    }

    /**
     *
     * @return Email blind carbon copy recipients.
     */
    @Nullable
    public List<Contact> getBcc() {
        return emailHeader.getBcc();
    }

    /**
     *
     * @return Email recipients.
     */
    @Nullable
    public List<Contact> getTo() {
        return emailHeader.getTo();
    }

    /**
     *
     * @return Email subject.
     */
    @NonNull
    public String getSubject() {
        return emailHeader.getSubject();
    }

    /**
     *
     * @return Email HTML.
     */
    @Nullable
    public String getHtml() {
        return html;
    }

    /**
     *
     * @return Email's text.
     */
    @Nullable
    public String getText() {
        return text;
    }

    /**
     *
     * @return Email sender
     */
    @NonNull
    public Contact getFrom() {
        return emailHeader.getFrom();
    }

    /**
     *
     * @return Email Reply-to
     */
    @Nullable
    public Contact getReplyTo() {
        return emailHeader.getReplyTo();
    }

    /**
     *
     * @return A Builder.
     */
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     *
     * @param emailHeader Email Sender, recipients and subjects
     * @return A Builder with Sender, recipients and subjects populated.
     */
    @NonNull
    public static Builder builder(@NonNull EmailHeader emailHeader) {
        Email.Builder builder = Email.builder()
                .subject(emailHeader.getSubject())
                .from(emailHeader.getFrom());
        if (emailHeader.getReplyTo() != null) {
            builder = builder.replyTo(emailHeader.getReplyTo());
        }
        if (emailHeader.getTo() != null) {
            for (Contact to : emailHeader.getTo()) {
                builder = builder.to(to);
            }
        }
        if (emailHeader.getCc() != null) {
            for (Contact cc : emailHeader.getCc()) {
                builder = builder.cc(cc);
            }
        }
        if (emailHeader.getBcc() != null) {
            for (Contact bcc : emailHeader.getBcc()) {
                builder = builder.bcc(bcc);
            }
        }
        return builder;
    }

    /**
     *
     * @return Whether to track if the email is opened
     */
    public boolean getTrackOpens() {
        return this.emailHeader.getTrackOpens();
    }

    /**
     *
     * @return Whether to track the email's links
     */
    @Nullable
    public TrackLinks getTrackLinks() {
        return this.emailHeader.getTrackLinks();
    }

    /**
     * TransactionEmail  builder.
     */
    public static class Builder {
        private EmailHeader.Builder builder;
        private String html;
        private String text;
        private List<Attachment> attachments;

        /**
         *
         * @param from sender email address
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder from(@NonNull String from) {
            builder = builder == null ? EmailHeader.builder().from(from) : builder.from(from);
            return this;
        }

        /**
         *
         * @param from sender email address
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder from(@NonNull Contact from) {
            builder = builder == null ? EmailHeader.builder().from(from) : builder.from(from);
            return this;
        }

        /**
         *
         * @param replyTo reply to email address
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder replyTo(@NonNull String replyTo) {
            builder = builder == null ? EmailHeader.builder().replyTo(replyTo) : builder.replyTo(replyTo);
            return this;
        }

        /**
         *
         * @param replyTo reply to contact
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder replyTo(@NonNull Contact replyTo) {
            builder = builder == null ? EmailHeader.builder().replyTo(replyTo) : builder.replyTo(replyTo);
            return this;
        }

        /**
         *
         * @param to Email recipient
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder to(@NonNull String to) {
            builder = builder == null ? EmailHeader.builder().to(to) : builder.to(to);
            return this;
        }

        /**
         *
         * @param to Email recipient
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder to(@NonNull Contact to) {
            builder = builder == null ? EmailHeader.builder().to(to) : builder.to(to);
            return this;
        }

        /**
         *
         * @param cc carbon copy recipient
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder cc(@NonNull Contact cc) {
            builder = builder == null ? EmailHeader.builder().cc(cc) : builder.cc(cc);
            return this;
        }

        /**
         *
         * @param bcc Blind carbon copy recipient
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder bcc(@NonNull Contact bcc) {
            builder = builder == null ? EmailHeader.builder().bcc(bcc) : builder.bcc(bcc);
            return this;
        }

        /**
         *
         * @param subject Email's subject
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder subject(@NonNull String subject) {
            builder = builder == null ? EmailHeader.builder().subject(subject) : builder.subject(subject);
            return this;
        }

        /**
         *
         * @param cc carbon copy email address
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder cc(@NonNull String cc) {
            builder = builder == null ? EmailHeader.builder().cc(cc) : builder.cc(cc);
            return this;
        }

        /**
         *
         * @param bcc blind carbon copy email address
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder bcc(@NonNull String bcc) {
            builder = builder == null ? EmailHeader.builder().bcc(bcc) : builder.bcc(bcc);
            return this;
        }

        /**
         *
         * @param trackLinks Whether email links should be tracked
         * @return Email Header Builder
         */
        public Builder trackLinks(TrackLinks trackLinks) {
            builder = builder == null ? EmailHeader.builder().trackLinks(trackLinks) : builder.trackLinks(trackLinks);
            return this;
        }

        /**
         *
         * @param trackOpens Whether the email needs to track the opening.
         * @return The Transactional Email Builder
         */
        public Builder trackOpens(boolean trackOpens) {
            builder = builder == null ? EmailHeader.builder().trackOpens(trackOpens) : builder.trackOpens(trackOpens);
            return this;
        }

        /**
         *
         * @param html Email's html
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder html(@Nullable String html) {
            this.html = html;
            return this;
        }

        /**
         *
         * @param text Email's text
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder text(@Nullable String text) {
            this.text = text;
            return this;
        }

        /**
         *
         * @param attachment Email's attachment
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder attachment(@NonNull Attachment attachment) {
            if (this.attachments == null) {
                attachments = new ArrayList<>();
            }
            attachments.add(attachment);
            return this;
        }

        /**
         *
         * @return a TransactionEmail
         */
        @NonNull
        public Email build() {
            if (StringUtils.isEmpty(html) && StringUtils.isEmpty(text)) {
                throw new IllegalArgumentException("you have to specify the email's content with Text, HTML or both");
            }
            return new Email(Objects.requireNonNull(builder).build(),
                    html,
                    text,
                    attachments);
        }
    }
}
