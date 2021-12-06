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
package io.micronaut.email.template;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.email.Attachment;
import io.micronaut.email.Contact;
import io.micronaut.email.EmailValidationUtils;
import io.micronaut.email.EmailWithoutContent;
import io.micronaut.email.EmailWithoutContentBuilder;
import io.micronaut.email.TrackLinks;
import io.micronaut.views.ModelAndView;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Email with text and html templates.
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <H> HTML model
 * @param <T> Text model
 */
@Introspected
public class Email<H, T> implements EmailWithoutContent {

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
    private final List<Attachment> attachments;

    @Nullable
    private final ModelAndView<T> text;

    @Nullable
    private final ModelAndView<H> html;

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
                 @Nullable ModelAndView<H> html,
                 @Nullable ModelAndView<T> text) {
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
    public ModelAndView<H> getHtml() {
        return html;
    }

    /**
     *
     * @return Email Text
     */
    @Nullable
    public ModelAndView<T> getText() {
        return text;
    }

    /**
     *
     * @param <H> HTML model
     * @param <T> Text model
     * @return The Builder
     */
    @NonNull
    public static <H, T> Builder<H, T> builder() {
        return new Builder<>();
    }

     /**
      * Email builder.
      * @param <H> HTML model
      * @param <T> Text model
     */
    public static class Builder<H, T> implements EmailWithoutContentBuilder<Email.Builder<H, T>, Email<H, T>> {
        private Contact from;
        private List<Contact> to;
        private String subject;
        private Contact replyTo;
        private List<Contact> cc;
        private List<Contact> bcc;
        private boolean trackOpens;
        private TrackLinks trackLinks;
        private List<Attachment> attachments;
        private ModelAndView<H> html;
        private ModelAndView<T> text;

        @Override
        @NonNull
        public Builder<H, T> trackLinks(@NonNull TrackLinks trackLinks) {
            this.trackLinks = trackLinks;
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> trackLinksInHtml() {
            this.trackLinks = TrackLinks.HTML;
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> trackLinksInText() {
            this.trackLinks = TrackLinks.TEXT;
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> trackLinksInHtmlAndText() {
            this.trackLinks = TrackLinks.HTML_AND_TEXT;
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> from(@NonNull String from) {
            this.from = new Contact(from);
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> from(@NonNull Contact from) {
            this.from = from;
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> replyTo(@NonNull String replyTo) {
            this.replyTo = new Contact(replyTo);
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> replyTo(@NonNull Contact replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> to(@NonNull String to) {
            if (this.to == null) {
                this.to = new ArrayList<>();
            }
            this.to.add(new Contact(to));
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> to(@NonNull Contact to) {
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
        public Builder<H, T> cc(@NonNull Contact cc) {
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
        public Builder<H, T> bcc(@NonNull Contact bcc) {
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
        public Builder<H, T> subject(@NonNull String subject) {
            this.subject = subject;
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> cc(@NonNull String cc) {
            if (this.cc == null) {
                this.cc = new ArrayList<>();
            }
            this.cc.add(new Contact(cc));
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> bcc(@NonNull String bcc) {
            if (this.bcc == null) {
                this.bcc = new ArrayList<>();
            }
            this.bcc.add(new Contact(bcc));
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> trackOpens(boolean trackOpens) {
            this.trackOpens = trackOpens;
            return this;
        }

        @Override
        @NonNull
        public Builder<H, T> attachment(@NonNull Attachment attachment) {
            if (this.attachments == null) {
                attachments = new ArrayList<>();
            }
            attachments.add(attachment);
            return this;
        }

        /**
         *
         * @param html Email's html
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder<H, T> html(@Nullable ModelAndView<H> html) {
            this.html = html;
            return this;
        }

        /**
         *
         * @param text Email's text
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder<H, T> text(@Nullable ModelAndView<T> text) {
            this.text = text;
            return this;
        }

        /**
         *
         * @param view view name to be rendered
         * @param model Model to be rendered against the view
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder<H, T> html(@NonNull String view, @NonNull H model) {
            this.html = new ModelAndView<>(view, model);
            return this;
        }

        /**
         *
         * @param view view name to be rendered
         * @param model Model to be rendered against the view
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder<H, T> text(@NonNull String view, @NonNull T model) {
            this.text = new ModelAndView<>(view, model);
            return this;
        }

        @Override
        @NonNull
        public Email<H, T> build() throws IllegalArgumentException {
            Email<H, T> email = new Email<>(from,
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

