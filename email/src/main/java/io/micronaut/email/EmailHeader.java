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
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Email components except content: sender, recipients and subject.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Introspected
public class EmailHeader {
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

    /**
     *
     * @param from Sender of the Email
     * @param replyTo Reply to
     * @param to To recipients
     * @param cc Carbon Copy recipients
     * @param bcc Blind Carbon Copy recipients
     * @param subject Subject
     */
    public EmailHeader(@NonNull Contact from,
                       @Nullable Contact replyTo,
                       @Nullable List<Contact> to,
                       @Nullable List<Contact> cc,
                       @Nullable List<Contact> bcc,
                       @NonNull String subject) {
        this.from = from;
        this.replyTo = replyTo;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
    }

    /**
     *
     * @return Email sender
     */
    @NonNull
    public Contact getFrom() {
        return from;
    }

    /**
     *
     * @return Email Reply-to
     */
    @Nullable
    public Contact getReplyTo() {
        return replyTo;
    }

    /**
     *
     * @return Email recipients.
     */
    @Nullable
    public List<Contact> getTo() {
        return to;
    }

    /**
     *
     * @return Email carbon copy recipients.
     */
    @Nullable
    public List<Contact> getCc() {
        return cc;
    }

    /**
     *
     * @return Email blind carbon copy recipients.
     */
    @Nullable
    public List<Contact> getBcc() {
        return bcc;
    }

    /**
     *
     * @return Email's subject
     */
    @NonNull
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @return Builder
     */
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * EmailHeader builder.
     */
    public static class Builder {
        private Contact from;
        private List<Contact> to;
        private String subject;
        private Contact replyTo;
        private List<Contact> cc;
        private List<Contact> bcc;

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
         * @return Builds an EmailHeader
         * @throws IllegalArgumentException If recipients are empty or subject or sender are missing
         */
        @NonNull
        public EmailHeader build() throws IllegalArgumentException {
            if (CollectionUtils.isEmpty(to) && CollectionUtils.isEmpty(cc) && CollectionUtils.isEmpty(bcc)) {
                throw new IllegalArgumentException("At least one to, cc or bcc recipient must be specified");
            }
            if (from == null || StringUtils.isEmpty(from.getEmail())) {
                throw new IllegalArgumentException("you have to specify the sender of the email");
            }
            if (StringUtils.isEmpty(subject)) {
                throw new IllegalArgumentException("you have to specify the email's subject");
            }
            return new EmailHeader(from,
                    replyTo,
                    to,
                    cc,
                    bcc,
                    subject);
        }

    }
}
