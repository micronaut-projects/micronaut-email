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
package io.micronaut.email.mailpit.client;

import io.micronaut.core.annotation.Experimental;
import io.micronaut.email.mailpit.client.model.MailpitAppInformation;
import io.micronaut.email.mailpit.client.model.MailpitChaosTriggers;
import io.micronaut.email.mailpit.client.model.MailpitDeleteMessagesRequest;
import io.micronaut.email.mailpit.client.model.MailpitHtmlCheckResponse;
import io.micronaut.email.mailpit.client.model.MailpitLinkCheckResponse;
import io.micronaut.email.mailpit.client.model.MailpitMessage;
import io.micronaut.email.mailpit.client.model.MailpitMessagesSummary;
import io.micronaut.email.mailpit.client.model.MailpitReleaseMessageRequest;
import io.micronaut.email.mailpit.client.model.MailpitRenameTagRequest;
import io.micronaut.email.mailpit.client.model.MailpitSendRequest;
import io.micronaut.email.mailpit.client.model.MailpitSendResponse;
import io.micronaut.email.mailpit.client.model.MailpitSetReadStatusRequest;
import io.micronaut.email.mailpit.client.model.MailpitSetTagsRequest;
import io.micronaut.email.mailpit.client.model.MailpitSpamAssassinResponse;
import io.micronaut.email.mailpit.client.model.MailpitWebUiConfiguration;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Declarative HTTP client for the Mailpit API v1.
 * @see <a href="https://mailpit.axllent.org/docs/api-v1/">Mailpit API V1</a>.
 *
 * @since 3.1.0
 */
@Experimental
@Client(id = "mailpit", errorType = String.class)
public interface MailpitClient {

    /**
     * Gets application information and mailbox totals.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/info">Get application information</a>.
     *
     * @return Application information.
     */
    @Get("/api/v1/info")
    MailpitAppInformation getInfo();

    /**
     * Gets web UI configuration.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/webui">Get web UI configuration</a>.
     *
     * @return Web UI configuration.
     */
    @Get("/api/v1/webui")
    MailpitWebUiConfiguration getWebUiConfiguration();

    /**
     * Gets current chaos trigger settings.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/chaos">Get Chaos triggers</a>.
     *
     * @return Chaos trigger settings.
     */
    @Get("/api/v1/chaos")
    MailpitChaosTriggers getChaos();

    /**
     * Sets current chaos trigger settings.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#put-/api/v1/chaos">Set Chaos triggers</a>.
     *
     * @param triggers Chaos trigger settings.
     * @return Updated chaos trigger settings.
     */
    @Put(value = "/api/v1/chaos", consumes = MediaType.APPLICATION_JSON)
    MailpitChaosTriggers setChaos(@Body MailpitChaosTriggers triggers);

    /**
     * Lists mailbox messages from newest to oldest.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/messages">List messages</a>.
     *
     * @param start Pagination offset.
     * @param limit Maximum number of messages.
     * @return Message summaries.
     */
    @Get("/api/v1/messages")
    MailpitMessagesSummary listMessages(@Nullable @QueryValue("start") Integer start,
                                        @Nullable @QueryValue("limit") Integer limit);

    /**
     * Sets read status for messages without a timezone query parameter.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#put-/api/v1/messages">Set read status</a>.
     *
     * @param request Read status request.
     * @return Plain text response.
     */
    @Put(value = "/api/v1/messages", consumes = MediaType.APPLICATION_JSON)
    String setReadStatus(@Body MailpitSetReadStatusRequest request);

    /**
     * Sets read status for messages.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#put-/api/v1/messages">Set read status</a>.
     *
     * @param request Read status request.
     * @param timezone Optional timezone identifier for search date filters.
     * @return Plain text response.
     */
    @Put(value = "/api/v1/messages", consumes = MediaType.APPLICATION_JSON)
    String setReadStatus(@Body MailpitSetReadStatusRequest request,
                         @Nullable @QueryValue("tz") String timezone);

    /**
     * Deletes messages.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#delete-/api/v1/messages">Delete messages</a>.
     *
     * @param request Delete messages request.
     * @return Plain text response.
     */
    @Delete(value = "/api/v1/messages", consumes = MediaType.APPLICATION_JSON)
    String deleteMessages(@Body MailpitDeleteMessagesRequest request);

    /**
     * Searches messages.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/search">Search messages</a>.
     *
     * @param query Search query.
     * @param start Pagination offset.
     * @param limit Maximum number of messages.
     * @param timezone Optional timezone identifier for date filters.
     * @return Message summaries.
     */
    @Get("/api/v1/search")
    MailpitMessagesSummary search(@QueryValue("query") String query,
                                  @Nullable @QueryValue("start") Integer start,
                                  @Nullable @QueryValue("limit") Integer limit,
                                  @Nullable @QueryValue("tz") String timezone);

    /**
     * Deletes messages matching a search.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#delete-/api/v1/search">Delete messages by search</a>.
     *
     * @param query Search query.
     * @param timezone Optional timezone identifier for date filters.
     * @return Plain text response.
     */
    @Delete(value = "/api/v1/search", produces = MediaType.TEXT_PLAIN)
    String deleteSearch(@QueryValue("query") String query,
                        @Nullable @QueryValue("tz") String timezone);

    /**
     * Gets a message and marks it read.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/message/%7BID%7D">Get message summary</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @return Message.
     */
    @Get("/api/v1/message/{id}")
    MailpitMessage getMessage(@PathVariable("id") String id);

    /**
     * Gets message headers.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/message/%7BID%7D/headers">Get message headers</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @return Message headers.
     */
    @Get("/api/v1/message/{id}/headers")
    Map<String, List<String>> getMessageHeaders(@PathVariable("id") String id);

    /**
     * Gets a message attachment or inline part.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/message/%7BID%7D/part/%7BPartID%7D">Get message attachment</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @param partId Attachment part ID.
     * @return Binary part response.
     */
    @Get("/api/v1/message/{id}/part/{partId}")
    HttpResponse<byte[]> getMessagePart(@PathVariable("id") String id,
                                        @PathVariable("partId") String partId);

    /**
     * Gets an attachment image thumbnail.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/message/%7BID%7D/part/%7BPartID%7D/thumb">Get an attachment image thumbnail</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @param partId Attachment part ID.
     * @return Binary thumbnail response.
     */
    @Get("/api/v1/message/{id}/part/{partId}/thumb")
    HttpResponse<byte[]> getMessagePartThumbnail(@PathVariable("id") String id,
                                                 @PathVariable("partId") String partId);

    /**
     * Gets a message's raw source.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/message/%7BID%7D/raw">Get message source</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @return Raw message source.
     */
    @Get(value = "/api/v1/message/{id}/raw", produces = MediaType.TEXT_PLAIN)
    String getRawMessage(@PathVariable("id") String id);

    /**
     * Releases a message through Mailpit's configured SMTP relay.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#post-/api/v1/message/%7BID%7D/release">Release message</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @param request Release message request.
     * @return Plain text response.
     */
    @Post(value = "/api/v1/message/{id}/release", consumes = MediaType.APPLICATION_JSON)
    String releaseMessage(@PathVariable("id") String id,
                          @Body MailpitReleaseMessageRequest request);

    /**
     * Runs Mailpit's HTML compatibility check for a message.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/message/%7BID%7D/html-check">HTML check</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @return HTML check response.
     */
    @Get("/api/v1/message/{id}/html-check")
    MailpitHtmlCheckResponse htmlCheck(@PathVariable("id") String id);

    /**
     * Runs Mailpit's link check for a message.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/message/%7BID%7D/link-check">Link check</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @param follow Whether redirects should be followed.
     * @return Link check response.
     */
    @Get("/api/v1/message/{id}/link-check")
    MailpitLinkCheckResponse linkCheck(@PathVariable("id") String id,
                                       @Nullable @QueryValue("follow") Boolean follow);

    /**
     * Runs SpamAssassin check for a message.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/message/%7BID%7D/sa-check">SpamAssassin check</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @return SpamAssassin response.
     */
    @Get("/api/v1/message/{id}/sa-check")
    MailpitSpamAssassinResponse spamAssassinCheck(@PathVariable("id") String id);

    /**
     * Sends a message through Mailpit's HTTP send API.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#post-/api/v1/send">Send a message</a>.
     *
     * @param request Send request.
     * @return Send response.
     */
    @Post(value = "/api/v1/send", consumes = MediaType.APPLICATION_JSON)
    MailpitSendResponse send(@Body MailpitSendRequest request);

    /**
     * Gets all current tags.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/tags">Get all current tags</a>.
     *
     * @return Tag names.
     */
    @Get("/api/v1/tags")
    List<String> getTags();

    /**
     * Sets tags for messages.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#put-/api/v1/tags">Set message tags</a>.
     *
     * @param request Set tags request.
     * @return Plain text response.
     */
    @Put(value = "/api/v1/tags", consumes = MediaType.APPLICATION_JSON, produces = MediaType.TEXT_PLAIN)
    String setMessageTags(@Body MailpitSetTagsRequest request);

    /**
     * Renames a tag.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#put-/api/v1/tags/%7BTag%7D">Rename a tag</a>.
     *
     * @param tag Current tag name.
     * @param request Rename request.
     * @return Plain text response.
     */
    @Put(value = "/api/v1/tags/{tag}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.TEXT_PLAIN)
    String renameTag(@PathVariable("tag") String tag,
                     @Body MailpitRenameTagRequest request);

    /**
     * Deletes a tag from all messages.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#delete-/api/v1/tags/%7BTag%7D">Delete a tag</a>.
     *
     * @param tag Tag name.
     * @return Plain text response.
     */
    @Delete(value = "/api/v1/tags/{tag}", produces = MediaType.TEXT_PLAIN)
    String deleteTag(@PathVariable("tag") String tag);

    /**
     * Renders the message HTML part.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/view/%7BID%7D.html">Render message HTML part</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @param embed Whether Mailpit should render iframe-friendly HTML.
     * @return Rendered HTML.
     */
    @Get(value = "/view/{id}.html", produces = MediaType.TEXT_HTML)
    String renderHtml(@PathVariable("id") String id,
                      @Nullable @QueryValue("embed") String embed);

    /**
     * Renders the message text part.
     * @see <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/view/%7BID%7D.txt">Render message text part</a>.
     *
     * @param id Message database ID or {@code latest}.
     * @return Rendered text.
     */
    @Get(value = "/view/{id}.txt", produces = MediaType.TEXT_PLAIN)
    String renderText(@PathVariable("id") String id);
}
