package io.micronaut.email.mailpit.client;

import io.micronaut.context.ApplicationContext;
import io.micronaut.email.mailpit.client.model.MailpitAttachment;
import io.micronaut.email.mailpit.client.model.MailpitDeleteMessagesRequest;
import io.micronaut.email.mailpit.client.model.MailpitMessage;
import io.micronaut.email.mailpit.client.model.MailpitMessageSummary;
import io.micronaut.email.mailpit.client.model.MailpitRenameTagRequest;
import io.micronaut.email.mailpit.client.model.MailpitSendAddress;
import io.micronaut.email.mailpit.client.model.MailpitSendAttachment;
import io.micronaut.email.mailpit.client.model.MailpitSendRequest;
import io.micronaut.email.mailpit.client.model.MailpitSetReadStatusRequest;
import io.micronaut.email.mailpit.client.model.MailpitSetTagsRequest;
import io.micronaut.email.test.Mailpit;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MailpitClientTest {

    private static final byte[] ATTACHMENT_CONTENT = "attachment content".getBytes(StandardCharsets.UTF_8);

    private GenericContainer<?> mailpit;
    private ApplicationContext applicationContext;
    private MailpitClient client;

    @BeforeEach
    void setUp() {
        Assumptions.assumeTrue(DockerClientFactory.instance().isDockerAvailable());
        applicationContext = ApplicationContext.run(Mailpit.getProperties().entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue

            )));
        client = applicationContext.getBean(MailpitClient.class);
        assertEquals("ok", client.deleteMessages(new MailpitDeleteMessagesRequest(List.of())));
    }

    @AfterEach
    void tearDown() {
        if (applicationContext != null) {
            applicationContext.close();
        }
        if (mailpit != null) {
            mailpit.stop();
        }
    }

    @Test
    void canSendInspectTagAndDeleteMessages() {
        String subject = "Mailpit API Test " + System.nanoTime();

        var sendResponse = client.send(new MailpitSendRequest(
            new MailpitSendAddress("sender@example.com", "Sender"),
            List.of(new MailpitSendAddress("receiver@example.com", "Receiver")),
            List.of(new MailpitSendAddress("manager@example.com", "Manager")),
            List.of("blind@example.com"),
            List.of(new MailpitSendAddress("reply@example.com", "Reply")),
            subject,
            "Plain body",
            "<strong>HTML body</strong>",
            List.of(new MailpitSendAttachment(
                Base64.getEncoder().encodeToString(ATTACHMENT_CONTENT),
                "note.txt",
                MediaType.TEXT_PLAIN,
                null
            )),
            List.of("ApiTest"),
            Map.of("X-Test-Header", "mailpit")
        ));

        String id = sendResponse.id();
        assertNotNull(id);
        assertTrue(client.getInfo().messages() >= 1);
        assertNotNull(client.getWebUiConfiguration());

        var messages = client.listMessages(0, 10);
        List<MailpitMessageSummary> summaries = Objects.requireNonNull(messages.messages());
        assertFalse(summaries.isEmpty());
        assertEquals(subject, summaries.get(0).subject());

        MailpitMessage message = client.getMessage(id);
        assertEquals(subject, message.subject());
        assertEquals("sender@example.com", Objects.requireNonNull(message.from()).address());
        assertEquals("Plain body", message.text());
        assertEquals("<strong>HTML body</strong>", message.html());
        assertTrue(Objects.requireNonNull(message.tags()).contains("ApiTest"));

        Map<String, List<String>> headers = client.getMessageHeaders(id);
        assertEquals(List.of(subject), headers.get("Subject"));
        assertTrue(client.getRawMessage(id).contains(subject));
        assertTrue(client.renderText(id).contains("Plain body"));
        assertTrue(client.renderHtml(id, null).contains("HTML body"));

        MailpitAttachment attachment = Objects.requireNonNull(message.attachments()).get(0);
        HttpResponse<byte[]> attachmentResponse = client.getMessagePart(id, Objects.requireNonNull(attachment.partId()));
        assertEquals(HttpStatus.OK, attachmentResponse.status());
        assertArrayEquals(ATTACHMENT_CONTENT, attachmentResponse.getBody().orElseThrow());

        assertEquals(1, client.search("tag:ApiTest", 0, 10, null).messagesCount());
        assertEquals("ok", client.setReadStatus(new MailpitSetReadStatusRequest(null, false, null), null));
        assertEquals("ok", client.setMessageTags(new MailpitSetTagsRequest(List.of(id), List.of("Updated"))));
        assertTrue(client.getTags().contains("Updated"));
        assertEquals("ok", client.renameTag("Updated", new MailpitRenameTagRequest("Renamed")));
        assertTrue(client.getTags().contains("Renamed"));
        assertEquals("ok", client.deleteTag("Renamed"));
        assertEquals("ok", client.deleteMessages(new MailpitDeleteMessagesRequest(List.of(id))));
        assertEquals(0, client.listMessages(0, 10).total());
    }
}
