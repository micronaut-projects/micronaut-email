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
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Representation of a transactional email.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Introspected
public class Email implements EmailWithoutContent {

    @NonNull
    @NotNull
    @Valid
    private final Contact from;

    @Nullable
    @Valid
    private final Contact replyTo;

    @Nullable
    @Valid
    private final List<Contact> to;

    @Nullable
    @Valid
    private final List<Contact> cc;

    @Nullable
    @Valid
    private final List<Contact> bcc;

    @NotBlank
    @NonNull
    private final String subject;

    private final boolean trackOpens;

    @Nullable
    private final TrackLinks trackLinks;

    @Nullable
    private List<Attachment> attachments;

    @Nullable
    private final String html;

    @Nullable
    private final String text;

    /**
     *
     * @param from Sender of the Email
     * @param replyTo Reply to
     * @param to To recipients
     * @param cc Carbon Copy recipients
     * @param bcc Blind Carbon Copy recipients
     * @param subject Subject
     * @param trackOpens Whether to track if the email is opened
     * @param trackLinks Whether to track the email's links
     * @param attachments Email attachments
     * @param html Email HTML
     * @param text Email Text
     */
    public Email(@NonNull Contact from,
                 @Nullable Contact replyTo,
                 @Nullable List<Contact> to,
                 @Nullable List<Contact> cc,
                 @Nullable List<Contact> bcc,
                 @NonNull String subject,
                 boolean trackOpens,
                 @Nullable TrackLinks trackLinks,
                 @Nullable List<Attachment> attachments,
                 @Nullable String html,
                 @Nullable String text) {
        this.from = from;
        this.replyTo = replyTo;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.trackOpens = trackOpens;
        this.trackLinks = trackLinks;
        this.attachments = attachments;
        this.html = html;
        this.text = text;
    }

    /**
     *
     * @param email Email without content.
     * @param html Email HTML
     * @param text Email Text
     */
    public Email(@NonNull EmailWithoutContent email,
                 @Nullable String html,
                 @Nullable String text) {
        this.from = email.getFrom();
        this.replyTo = email.getReplyTo();
        this.to = email.getTo();
        this.cc = email.getCc();
        this.bcc = email.getBcc();
        this.subject = email.getSubject();
        this.trackOpens = email.getTrackOpens();
        this.trackLinks = email.getTrackLinks();
        this.attachments = email.getAttachments();
        this.html = html;
        this.text = text;
    }

    @Override
    @NonNull
    public Contact getFrom() {
        return from;
    }

    @Override
    @Nullable
    public Contact getReplyTo() {
        return replyTo;
    }

    @Override
    @Nullable
    public List<Contact> getTo() {
        return to;
    }

    @Override
    @Nullable
    public List<Contact> getCc() {
        return cc;
    }

    @Override
    @Nullable
    public List<Contact> getBcc() {
        return bcc;
    }

    @Override
    @NonNull
    public String getSubject() {
        return subject;
    }

    @Override
    public boolean getTrackOpens() {
        return trackOpens;
    }

    @Override
    @Nullable
    public TrackLinks getTrackLinks() {
        return trackLinks;
    }

    @Override
    @Nullable
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     *
     * @return Email HTML
     */
    @Nullable
    public String getHtml() {
        return html;
    }

    /**
     *
     * @return Email Text
     */
    @Nullable
    public String getText() {
        return text;
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
    public static class Builder implements EmailWithoutContentBuilder<Email.Builder, Email> {
        private Contact from;
        private List<Contact> to;
        private String subject;
        private Contact replyTo;
        private List<Contact> cc;
        private List<Contact> bcc;
        private boolean trackOpens;
        private TrackLinks trackLinks;
        private List<Attachment> attachments;
        private String html;
        private String text;

        @Override
        @NonNull
        public Email.Builder trackLinks(@NonNull TrackLinks trackLinks) {
            this.trackLinks = trackLinks;
            return this;
        }

        @Override
        @NonNull
        public Email.Builder trackLinksInHtml() {
            this.trackLinks = TrackLinks.HTML;
            return this;
        }

        @Override
        @NonNull
        public Email.Builder trackLinksInText() {
            this.trackLinks = TrackLinks.TEXT;
            return this;
        }

        @Override
        @NonNull
        public Email.Builder trackLinksInHtmlAndText() {
            this.trackLinks = TrackLinks.HTML_AND_TEXT;
            return this;
        }

        @Override
        @NonNull
        public Email.Builder from(@NonNull String from) {
            this.from = new Contact(from);
            return this;
        }

        @Override
        @NonNull
        public Email.Builder from(@NonNull Contact from) {
            this.from = from;
            return this;
        }

        @Override
        @NonNull
        public Email.Builder replyTo(@NonNull String replyTo) {
            this.replyTo = new Contact(replyTo);
            return this;
        }

        @Override
        @NonNull
        public Email.Builder replyTo(@NonNull Contact replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        @Override
        @NonNull
        public Email.Builder to(@NonNull String to) {
            if (this.to == null) {
                this.to = new ArrayList<>();
            }
            this.to.add(new Contact(to));
            return this;
        }

        @Override
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

        @Override
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

        @Override
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

        @Override
        @NonNull
        public Email.Builder subject(@NonNull String subject) {
            this.subject = subject;
            return this;
        }

        @Override
        @NonNull
        public Email.Builder cc(@NonNull String cc) {
            if (this.cc == null) {
                this.cc = new ArrayList<>();
            }
            this.cc.add(new Contact(cc));
            return this;
        }

        @Override
        @NonNull
        public Email.Builder bcc(@NonNull String bcc) {
            if (this.bcc == null) {
                this.bcc = new ArrayList<>();
            }
            this.bcc.add(new Contact(bcc));
            return this;
        }

        @Override
        @NonNull
        public Email.Builder trackOpens(boolean trackOpens) {
            this.trackOpens = trackOpens;
            return this;
        }

        @Override
        @NonNull
        public Email.Builder attachment(@NonNull Attachment attachment) {
            if (this.attachments == null) {
                attachments = new ArrayList<>();
            }
            attachments.add(attachment);
            return this;
        }

        @Override
        @NonNull
        public Email.Builder attachment(@NonNull Consumer<Attachment.Builder> attachment) {
            Attachment.Builder builder = Attachment.builder();
            attachment.accept(builder);
            return attachment(builder.build());
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

        @Override
        @NonNull
        public Email build() throws IllegalArgumentException {
            Email email = new Email(from,
                    replyTo,
                    to,
                    cc,
                    bcc,
                    subject,
                    trackOpens,
                    trackLinks,
                    attachments,
                    html,
                    text);
            EmailValidationUtils.validate(email);

            return email;
        }
    }
}
