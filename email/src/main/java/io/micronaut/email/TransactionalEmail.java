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
import java.util.Objects;

/**
 * Representation of a transactional email.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Introspected
public class TransactionalEmail {

    @NonNull
    @NotNull
    @Valid
    private final Sender sender;

    @NonNull
    @NotNull
    @Valid
    private final Recipient recipient;

    @NonNull
    @NotBlank
    private final String subject;

    @Nullable
    private final String html;

    @Nullable
    private final String text;

    /**
     *
     * @param from Sender of the Email
     * @param to Email's recipient
     * @param subject Email's subject
     */
    public TransactionalEmail(@NonNull Contact from,
                              @NonNull List<Contact> to,
                              @NonNull String subject) {
        this(from, to, subject, null, null, null, null, null);
    }

    /**
     *
     * @param from Sender of the Email
     * @param to Email's recipient
     * @param subject Email's subject
     * @param replyTo Reply to
     * @param cc Carbon copy
     * @param bcc Blind Carbon copy
     * @param html Email content as HTML
     * @param text Email content as text
     */
    public TransactionalEmail(@NonNull Contact from,
                 @NonNull List<Contact> to,
                 @NonNull String subject,
                 @Nullable Contact replyTo,
                 @Nullable List<Contact> cc,
                 @Nullable List<Contact> bcc,
                 @Nullable String html,
                 @Nullable String text) {

        this.subject = subject;
        this.sender = new Sender(from, replyTo);
        this.recipient = new Recipient(to, cc, bcc);
        this.html = html;
        this.text = text;
    }

    /**
     *
     * @return The Email' sender (from and reply to emails)
     */
    @NonNull
    public Sender getSender() {
        return sender;
    }

    /**
     *
     * @return The Email' recpient (to, cc and bcc emails)
     */
    @NonNull
    public Recipient getRecipient() {
        return recipient;
    }

    /**
     *
     * @return Email carbon copy recipients.
     */
    @Nullable
    public List<Contact> getCc() {
        return getRecipient().getCc();
    }

    /**
     *
     * @return Email blind carbon copy recipients.
     */
    @Nullable
    public List<Contact> getBcc() {
        return getRecipient().getBcc();
    }

    /**
     *
     * @return Email recipients.
     */
    @NonNull
    public List<Contact> getTo() {
        return getRecipient().getTo();
    }

    /**
     *
     * @return Email subject.
     */
    @NonNull
    public String getSubject() {
        return subject;
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
        return getSender().getFrom();
    }

    /**
     *
     * @return Email Reply-to
     */
    @Nullable
    public Contact getReplyTo() {
        return getSender().getReplyTo();
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
     * TransactionEmail  builder.
     */
    public static class Builder {
        private Contact from;
        private List<Contact> to;
        private String subject;
        private Contact replyTo;
        private List<Contact> cc;
        private List<Contact> bcc;
        private String html;
        private String text;

        /**
         *
         * @param from sender email address
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder from(@NonNull String from) {
            this.from = new Contact(from);
            return this;
        }

        /**
         *
         * @param from sender email address
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder from(@NonNull Contact from) {
            this.from = from;
            return this;
        }

        /**
         *
         * @param replyTo reply to email address
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder replyTo(@NonNull String replyTo) {
            this.replyTo = new Contact(replyTo);
            return this;
        }

        /**
         *
         * @param replyTo reply to contact
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder replyTo(@NonNull Contact replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        /**
         *
         * @param to Email recipient
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder to(@NonNull String to) {
            if (this.to == null) {
                this.to = new ArrayList<>();
            }
            this.to.add(new Contact(to));
            return this;
        }

        /**
         *
         * @param to Email recipient
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder to(@NonNull Contact to) {
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
         * @param cc carbon copy recipient
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder cc(@NonNull Contact cc) {
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
         * @param bcc Blind carbon copy recipient
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder bcc(@NonNull Contact bcc) {
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
         * @param subject Email's subject
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder subject(@NonNull String subject) {
            this.subject = subject;
            return this;
        }

        /**
         *
         * @param cc carbon copy email address
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder cc(@NonNull String cc) {
            if (this.cc == null) {
                this.cc = new ArrayList<>();
            }
            this.cc.add(new Contact(cc));
            return this;
        }

        /**
         *
         * @param bcc blind carbon copy email address
         * @return The Transactional Email Builder
         */
        @NonNull
        public Builder bcc(@NonNull String bcc) {
            if (this.bcc == null) {
                this.bcc = new ArrayList<>();
            }
            this.bcc.add(new Contact(bcc));
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
         * @return a TransactionEmail
         */
        @NonNull
        public TransactionalEmail build() {
            return new TransactionalEmail(Objects.requireNonNull(from),
                    Objects.requireNonNull(to),
                    Objects.requireNonNull(subject),
                    replyTo,
                    cc,
                    bcc,
                    html,
                    text);
        }

        /**
         *
         * @param recipient The email recipient (to, cc, bcc).
         * @return The Builder
         */
        @NonNull
        public Builder recipient(@NonNull Recipient recipient) {
            for (Contact to : recipient.getTo()) {
                to(to);
            }
            if (recipient.getBcc() != null) {
                for (Contact bcc : recipient.getBcc()) {
                    addBcc(bcc);
                }
            }
            if (recipient.getCc() != null) {
                for (Contact cc : recipient.getCc()) {
                    addCc(cc);
                }
            }
            return this;
        }
    }
}
