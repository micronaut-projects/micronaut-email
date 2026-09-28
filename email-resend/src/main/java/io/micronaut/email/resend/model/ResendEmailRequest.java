/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.email.resend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Request of the Resend send email API.
 * @see <a href="https://resend.com/docs/api-reference/emails/send-email">Send Email</a>
 *
 * @param from Sender email address. It supports the format {@code Name <email@example.com>}.
 * @param to Recipients.
 * @param subject Subject.
 * @param cc Cc recipients.
 * @param bcc Bcc recipients.
 * @param replyTo Reply-To addresses.
 * @param html HTML body.
 * @param text Text body.
 * @param headers Custom email headers.
 * @param attachments Attachments.
 * @param tags Tags.
 * @param scheduledAt When to send the email. Either natural language, such as {@code in 1 min}, or an ISO 8601 date.
 * @param topicId Topic ID.
 * @param template Template. It cannot be combined with an HTML or text body.
 * @since 3.3.0
 */
@Serdeable
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ResendEmailRequest(
    @JsonProperty("from") @Nullable String from,
    @JsonProperty("to") @Nullable List<String> to,
    @JsonProperty("subject") @Nullable String subject,
    @JsonProperty("cc") @Nullable List<String> cc,
    @JsonProperty("bcc") @Nullable List<String> bcc,
    @JsonProperty("reply_to") @Nullable List<String> replyTo,
    @JsonProperty("html") @Nullable String html,
    @JsonProperty("text") @Nullable String text,
    @JsonProperty("headers") @Nullable Map<String, String> headers,
    @JsonProperty("attachments") @Nullable List<ResendAttachment> attachments,
    @JsonProperty("tags") @Nullable List<ResendTag> tags,
    @JsonProperty("scheduled_at") @Nullable String scheduledAt,
    @JsonProperty("topic_id") @Nullable String topicId,
    @JsonProperty("template") @Nullable ResendTemplate template
) {

    /**
     *
     * @return A {@link ResendEmailRequest} builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@link ResendEmailRequest} builder.
     *
     * @since 3.3.0
     */
    public static final class Builder {
        private @Nullable String from;
        private @Nullable List<String> to;
        private @Nullable String subject;
        private @Nullable List<String> cc;
        private @Nullable List<String> bcc;
        private @Nullable List<String> replyTo;
        private @Nullable String html;
        private @Nullable String text;
        private @Nullable Map<String, String> headers;
        private @Nullable List<ResendAttachment> attachments;
        private @Nullable List<ResendTag> tags;
        private @Nullable String scheduledAt;
        private @Nullable String topicId;
        private @Nullable ResendTemplate template;
        private @Nullable String idempotencyKey;

        private Builder() {
        }

        /**
         *
         * @param from Sender email address. It supports the format {@code Name <email@example.com>}.
         * @return The builder
         */
        public Builder from(@Nullable String from) {
            this.from = from;
            return this;
        }

        /**
         *
         * @param to Recipients. They replace any recipient previously set.
         * @return The builder
         */
        public Builder to(@Nullable List<String> to) {
            this.to = copy(to);
            return this;
        }

        /**
         *
         * @param to Recipient to add.
         * @return The builder
         */
        public Builder addTo(String to) {
            if (this.to == null) {
                this.to = new ArrayList<>();
            }
            this.to.add(to);
            return this;
        }

        /**
         *
         * @param subject Subject.
         * @return The builder
         */
        public Builder subject(@Nullable String subject) {
            this.subject = subject;
            return this;
        }

        /**
         *
         * @param cc Cc recipients. They replace any Cc recipient previously set.
         * @return The builder
         */
        public Builder cc(@Nullable List<String> cc) {
            this.cc = copy(cc);
            return this;
        }

        /**
         *
         * @param cc Cc recipient to add.
         * @return The builder
         */
        public Builder addCc(String cc) {
            if (this.cc == null) {
                this.cc = new ArrayList<>();
            }
            this.cc.add(cc);
            return this;
        }

        /**
         *
         * @param bcc Bcc recipients. They replace any Bcc recipient previously set.
         * @return The builder
         */
        public Builder bcc(@Nullable List<String> bcc) {
            this.bcc = copy(bcc);
            return this;
        }

        /**
         *
         * @param bcc Bcc recipient to add.
         * @return The builder
         */
        public Builder addBcc(String bcc) {
            if (this.bcc == null) {
                this.bcc = new ArrayList<>();
            }
            this.bcc.add(bcc);
            return this;
        }

        /**
         *
         * @param replyTo Reply-To addresses. They replace any Reply-To address previously set.
         * @return The builder
         */
        public Builder replyTo(@Nullable List<String> replyTo) {
            this.replyTo = copy(replyTo);
            return this;
        }

        /**
         *
         * @param replyTo Reply-To address to add.
         * @return The builder
         */
        public Builder addReplyTo(String replyTo) {
            if (this.replyTo == null) {
                this.replyTo = new ArrayList<>();
            }
            this.replyTo.add(replyTo);
            return this;
        }

        /**
         *
         * @param html HTML body.
         * @return The builder
         */
        public Builder html(@Nullable String html) {
            this.html = html;
            return this;
        }

        /**
         *
         * @param text Text body.
         * @return The builder
         */
        public Builder text(@Nullable String text) {
            this.text = text;
            return this;
        }

        /**
         *
         * @param headers Custom email headers. They replace any header previously set.
         * @return The builder
         */
        public Builder headers(@Nullable Map<String, String> headers) {
            this.headers = headers == null ? null : new LinkedHashMap<>(headers);
            return this;
        }

        /**
         *
         * @param name Name of the custom email header to add.
         * @param value Value of the custom email header to add.
         * @return The builder
         */
        public Builder header(String name, String value) {
            if (this.headers == null) {
                this.headers = new LinkedHashMap<>();
            }
            this.headers.put(name, value);
            return this;
        }

        /**
         *
         * @param attachments Attachments. They replace any attachment previously set.
         * @return The builder
         */
        public Builder attachments(@Nullable List<ResendAttachment> attachments) {
            this.attachments = copy(attachments);
            return this;
        }

        /**
         *
         * @param attachment Attachment to add.
         * @return The builder
         */
        public Builder attachment(ResendAttachment attachment) {
            if (this.attachments == null) {
                this.attachments = new ArrayList<>();
            }
            this.attachments.add(attachment);
            return this;
        }

        /**
         *
         * @param tags Tags. They replace any tag previously set.
         * @return The builder
         */
        public Builder tags(@Nullable List<ResendTag> tags) {
            this.tags = copy(tags);
            return this;
        }

        /**
         *
         * @param name Name of the tag to add.
         * @param value Value of the tag to add.
         * @return The builder
         */
        public Builder tag(String name, String value) {
            if (this.tags == null) {
                this.tags = new ArrayList<>();
            }
            this.tags.add(new ResendTag(name, value));
            return this;
        }

        /**
         *
         * @param scheduledAt When to send the email. Either natural language, such as {@code in 1 min}, or an ISO 8601 date.
         * @return The builder
         */
        public Builder scheduledAt(@Nullable String scheduledAt) {
            this.scheduledAt = scheduledAt;
            return this;
        }

        /**
         *
         * @param scheduledAt When to send the email.
         * @return The builder
         */
        public Builder scheduledAt(Instant scheduledAt) {
            this.scheduledAt = scheduledAt.toString();
            return this;
        }

        /**
         *
         * @param topicId Topic ID.
         * @return The builder
         */
        public Builder topicId(@Nullable String topicId) {
            this.topicId = topicId;
            return this;
        }

        /**
         *
         * @param template Template. It cannot be combined with an HTML or text body.
         * @return The builder
         */
        public Builder template(@Nullable ResendTemplate template) {
            this.template = template;
            return this;
        }

        /**
         * The idempotency key is sent as the {@code Idempotency-Key} HTTP header. It is not part of the {@link ResendEmailRequest}.
         * @see <a href="https://resend.com/docs/dashboard/emails/idempotency-keys">Idempotency keys</a>
         *
         * @param idempotencyKey Unique key, up to 256 characters, to prevent sending the same email twice.
         * @return The builder
         */
        public Builder idempotencyKey(@Nullable String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        /**
         *
         * @return The idempotency key.
         */
        public @Nullable String getIdempotencyKey() {
            return idempotencyKey;
        }

        /**
         *
         * @return A {@link ResendEmailRequest}.
         */
        public ResendEmailRequest build() {
            return new ResendEmailRequest(
                from,
                copy(to),
                subject,
                copy(cc),
                copy(bcc),
                copy(replyTo),
                html,
                text,
                headers == null ? null : new LinkedHashMap<>(headers),
                copy(attachments),
                copy(tags),
                scheduledAt,
                topicId,
                template
            );
        }

        private static <T> @Nullable List<T> copy(@Nullable List<T> list) {
            return list == null ? null : new ArrayList<>(list);
        }
    }
}
