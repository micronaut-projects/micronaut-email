package io.micronaut.email.mailpit.client.model;

import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class MailpitModelSerializationTest {

    @Inject
    JsonMapper jsonMapper;

    @TestFactory
    Stream<DynamicTest> modelsSerializeToExpectedJson() {
        return serializationCases()
            .map(testCase -> DynamicTest.dynamicTest(testCase.name(), () -> assertEquals(
                testCase.expectedJson(),
                jsonMapper.writeValueAsString(testCase.value())
            )));
    }

    private static Stream<SerializationCase> serializationCases() {
        return Stream.of(
            new SerializationCase(
                "MailpitAddress",
                address(),
                """
                {"Address":"sender@example.com","Name":"Sender"}"""
            ),
            new SerializationCase(
                "MailpitAppInformation",
                appInformation(),
                """
                {"Version":"1.27.8","LatestVersion":"1.28.0","Database":"/tmp/mailpit.db","DatabaseSize":1024,"Messages":5,"Unread":2,"Tags":{"Important":3},"RuntimeStats":{"Uptime":10,"Memory":2048,"MessagesDeleted":1,"SMTPAccepted":2,"SMTPAcceptedSize":512,"SMTPRejected":3,"SMTPIgnored":4}}"""
            ),
            new SerializationCase(
                "MailpitAttachment",
                attachment(),
                """
                {"PartID":"part-1","FileName":"note.txt","ContentType":"text/plain","ContentID":"content-1","Size":32,"Checksums":{"MD5":"md5","SHA1":"sha1","SHA256":"sha256"}}"""
            ),
            new SerializationCase(
                "MailpitAttachmentChecksums",
                checksums(),
                """
                {"MD5":"md5","SHA1":"sha1","SHA256":"sha256"}"""
            ),
            new SerializationCase(
                "MailpitChaosTrigger",
                chaosTrigger(),
                """
                {"ErrorCode":451,"Probability":25}"""
            ),
            new SerializationCase(
                "MailpitChaosTriggers",
                new MailpitChaosTriggers(chaosTrigger(), new MailpitChaosTrigger(452, 50), new MailpitChaosTrigger(535, 75)),
                """
                {"Sender":{"ErrorCode":451,"Probability":25},"Recipient":{"ErrorCode":452,"Probability":50},"Authentication":{"ErrorCode":535,"Probability":75}}"""
            ),
            new SerializationCase(
                "MailpitDeleteMessagesRequest",
                new MailpitDeleteMessagesRequest(List.of("message-1", "message-2")),
                """
                {"IDs":["message-1","message-2"]}"""
            ),
            new SerializationCase(
                "MailpitHtmlCheckResponse",
                new MailpitHtmlCheckResponse(
                    List.of(htmlCheckWarning()),
                    Map.of("desktop", List.of("Outlook")),
                    htmlCheckTotal()
                ),
                """
                {"Warnings":[{"Slug":"css-position","Title":"CSS position","Description":"Position is not supported","URL":"https://example.com/css-position","Category":"CSS","Tags":["css"],"Keywords":"position","Results":[{"Name":"Outlook","Platform":"Windows","Family":"Outlook","Version":"2021","Support":"unsupported","NoteNumber":"1"}],"NotesByNumber":{"1":"Avoid position"},"Score":{"Found":1,"Supported":60.0,"Partial":20.0,"Unsupported":20.0}}],"Platforms":{"desktop":["Outlook"]},"Total":{"Tests":10,"Nodes":20,"Supported":70.0,"Partial":20.0,"Unsupported":10.0}}"""
            ),
            new SerializationCase(
                "MailpitHtmlCheckResult",
                htmlCheckResult(),
                """
                {"Name":"Outlook","Platform":"Windows","Family":"Outlook","Version":"2021","Support":"unsupported","NoteNumber":"1"}"""
            ),
            new SerializationCase(
                "MailpitHtmlCheckScore",
                htmlCheckScore(),
                """
                {"Found":1,"Supported":60.0,"Partial":20.0,"Unsupported":20.0}"""
            ),
            new SerializationCase(
                "MailpitHtmlCheckTotal",
                htmlCheckTotal(),
                """
                {"Tests":10,"Nodes":20,"Supported":70.0,"Partial":20.0,"Unsupported":10.0}"""
            ),
            new SerializationCase(
                "MailpitHtmlCheckWarning",
                htmlCheckWarning(),
                """
                {"Slug":"css-position","Title":"CSS position","Description":"Position is not supported","URL":"https://example.com/css-position","Category":"CSS","Tags":["css"],"Keywords":"position","Results":[{"Name":"Outlook","Platform":"Windows","Family":"Outlook","Version":"2021","Support":"unsupported","NoteNumber":"1"}],"NotesByNumber":{"1":"Avoid position"},"Score":{"Found":1,"Supported":60.0,"Partial":20.0,"Unsupported":20.0}}"""
            ),
            new SerializationCase(
                "MailpitLink",
                link(),
                """
                {"URL":"https://example.com","StatusCode":200,"Status":"OK"}"""
            ),
            new SerializationCase(
                "MailpitLinkCheckResponse",
                new MailpitLinkCheckResponse(0, List.of(link())),
                """
                {"Errors":0,"Links":[{"URL":"https://example.com","StatusCode":200,"Status":"OK"}]}"""
            ),
            new SerializationCase(
                "MailpitListUnsubscribe",
                listUnsubscribe(),
                """
                {"Header":"<mailto:unsubscribe@example.com>","Links":["mailto:unsubscribe@example.com"],"Errors":"none","HeaderPost":"List-Unsubscribe=One-Click"}"""
            ),
            new SerializationCase(
                "MailpitMessage",
                message(),
                """
                {"ID":"message-1","MessageID":"<message-1@example.com>","From":{"Address":"sender@example.com","Name":"Sender"},"To":[{"Address":"sender@example.com","Name":"Sender"}],"Cc":[{"Address":"sender@example.com","Name":"Sender"}],"Bcc":[{"Address":"sender@example.com","Name":"Sender"}],"ReplyTo":[{"Address":"sender@example.com","Name":"Sender"}],"ReturnPath":"bounce@example.com","Subject":"Subject","ListUnsubscribe":{"Header":"<mailto:unsubscribe@example.com>","Links":["mailto:unsubscribe@example.com"],"Errors":"none","HeaderPost":"List-Unsubscribe=One-Click"},"Date":"2026-05-29T12:00:00Z","Tags":["Important"],"Username":"user","Text":"Plain body","HTML":"<p>HTML body</p>","Size":512,"Inline":[{"PartID":"part-1","FileName":"note.txt","ContentType":"text/plain","ContentID":"content-1","Size":32,"Checksums":{"MD5":"md5","SHA1":"sha1","SHA256":"sha256"}}],"Attachments":[{"PartID":"part-1","FileName":"note.txt","ContentType":"text/plain","ContentID":"content-1","Size":32,"Checksums":{"MD5":"md5","SHA1":"sha1","SHA256":"sha256"}}]}"""
            ),
            new SerializationCase(
                "MailpitMessageRelayConfiguration",
                messageRelayConfiguration(),
                """
                {"Enabled":true,"SMTPServer":"smtp.example.com:587","ReturnPath":"bounce@example.com","AllowedRecipients":"*@example.com","BlockedRecipients":"blocked@example.com","OverrideFrom":"sender@example.com","PreserveMessageIDs":true,"RecipientAllowlist":"*@example.com"}"""
            ),
            new SerializationCase(
                "MailpitMessagesSummary",
                new MailpitMessagesSummary(5, 2, 1, 5, 2, 0, List.of("Important"), List.of(messageSummary())),
                """
                {"total":5,"unread":2,"count":1,"messages_count":5,"messages_unread":2,"start":0,"tags":["Important"],"messages":[{"ID":"message-1","MessageID":"<message-1@example.com>","Read":false,"From":{"Address":"sender@example.com","Name":"Sender"},"To":[{"Address":"sender@example.com","Name":"Sender"}],"Cc":[{"Address":"sender@example.com","Name":"Sender"}],"Bcc":[{"Address":"sender@example.com","Name":"Sender"}],"ReplyTo":[{"Address":"sender@example.com","Name":"Sender"}],"Subject":"Subject","Created":"2026-05-29T12:00:00Z","Username":"user","Tags":["Important"],"Size":512,"Attachments":1,"Snippet":"Plain body"}]}"""
            ),
            new SerializationCase(
                "MailpitMessageSummary",
                messageSummary(),
                """
                {"ID":"message-1","MessageID":"<message-1@example.com>","Read":false,"From":{"Address":"sender@example.com","Name":"Sender"},"To":[{"Address":"sender@example.com","Name":"Sender"}],"Cc":[{"Address":"sender@example.com","Name":"Sender"}],"Bcc":[{"Address":"sender@example.com","Name":"Sender"}],"ReplyTo":[{"Address":"sender@example.com","Name":"Sender"}],"Subject":"Subject","Created":"2026-05-29T12:00:00Z","Username":"user","Tags":["Important"],"Size":512,"Attachments":1,"Snippet":"Plain body"}"""
            ),
            new SerializationCase(
                "MailpitReleaseMessageRequest",
                new MailpitReleaseMessageRequest(List.of("receiver@example.com")),
                """
                {"To":["receiver@example.com"]}"""
            ),
            new SerializationCase(
                "MailpitRenameTagRequest",
                new MailpitRenameTagRequest("Updated"),
                """
                {"Name":"Updated"}"""
            ),
            new SerializationCase(
                "MailpitRuntimeStats",
                runtimeStats(),
                """
                {"Uptime":10,"Memory":2048,"MessagesDeleted":1,"SMTPAccepted":2,"SMTPAcceptedSize":512,"SMTPRejected":3,"SMTPIgnored":4}"""
            ),
            new SerializationCase(
                "MailpitSendAddress",
                sendAddress(),
                """
                {"Email":"receiver@example.com","Name":"Receiver"}"""
            ),
            new SerializationCase(
                "MailpitSendAttachment",
                sendAttachment(),
                """
                {"Content":"YXR0YWNobWVudA==","Filename":"note.txt","ContentType":"text/plain","ContentID":"content-1"}"""
            ),
            new SerializationCase(
                "MailpitSendRequest",
                sendRequest(),
                """
                {"From":{"Email":"receiver@example.com","Name":"Receiver"},"To":[{"Email":"receiver@example.com","Name":"Receiver"}],"Cc":[{"Email":"receiver@example.com","Name":"Receiver"}],"Bcc":["bcc@example.com"],"ReplyTo":[{"Email":"receiver@example.com","Name":"Receiver"}],"Subject":"Subject","Text":"Plain body","HTML":"<p>HTML body</p>","Attachments":[{"Content":"YXR0YWNobWVudA==","Filename":"note.txt","ContentType":"text/plain","ContentID":"content-1"}],"Tags":["Important"],"Headers":{"X-Test":"mailpit"}}"""
            ),
            new SerializationCase(
                "MailpitSendResponse",
                new MailpitSendResponse("message-1"),
                """
                {"ID":"message-1"}"""
            ),
            new SerializationCase(
                "MailpitSetReadStatusRequest",
                new MailpitSetReadStatusRequest(List.of("message-1"), true, "tag:Important"),
                """
                {"IDs":["message-1"],"Read":true,"Search":"tag:Important"}"""
            ),
            new SerializationCase(
                "MailpitSetTagsRequest",
                new MailpitSetTagsRequest(List.of("message-1"), List.of("Important")),
                """
                {"IDs":["message-1"],"Tags":["Important"]}"""
            ),
            new SerializationCase(
                "MailpitSpamAssassinResponse",
                new MailpitSpamAssassinResponse("spam detected", true, List.of(spamAssassinRule()), 5.5),
                """
                {"Error":"spam detected","IsSpam":true,"Rules":[{"Description":"Bayesian spam probability","Name":"BAYES_99","Score":5.5}],"Score":5.5}"""
            ),
            new SerializationCase(
                "MailpitSpamAssassinRule",
                spamAssassinRule(),
                """
                {"Description":"Bayesian spam probability","Name":"BAYES_99","Score":5.5}"""
            ),
            new SerializationCase(
                "MailpitWebUiConfiguration",
                new MailpitWebUiConfiguration("Local Mailpit", messageRelayConfiguration(), true, false, true, false),
                """
                {"Label":"Local Mailpit","MessageRelay":{"Enabled":true,"SMTPServer":"smtp.example.com:587","ReturnPath":"bounce@example.com","AllowedRecipients":"*@example.com","BlockedRecipients":"blocked@example.com","OverrideFrom":"sender@example.com","PreserveMessageIDs":true,"RecipientAllowlist":"*@example.com"},"SpamAssassin":true,"ChaosEnabled":false,"DuplicatesIgnored":true,"HideDeleteAllButton":false}"""
            )
        );
    }

    private static MailpitAddress address() {
        return new MailpitAddress("sender@example.com", "Sender");
    }

    private static MailpitAppInformation appInformation() {
        return new MailpitAppInformation(
            "1.27.8",
            "1.28.0",
            "/tmp/mailpit.db",
            1024,
            5,
            2,
            Map.of("Important", 3L),
            runtimeStats()
        );
    }

    private static MailpitAttachment attachment() {
        return new MailpitAttachment("part-1", "note.txt", "text/plain", "content-1", 32, checksums());
    }

    private static MailpitAttachmentChecksums checksums() {
        return new MailpitAttachmentChecksums("md5", "sha1", "sha256");
    }

    private static MailpitChaosTrigger chaosTrigger() {
        return new MailpitChaosTrigger(451, 25);
    }

    private static MailpitHtmlCheckResult htmlCheckResult() {
        return new MailpitHtmlCheckResult("Outlook", "Windows", "Outlook", "2021", "unsupported", "1");
    }

    private static MailpitHtmlCheckScore htmlCheckScore() {
        return new MailpitHtmlCheckScore(1, 60.0, 20.0, 20.0);
    }

    private static MailpitHtmlCheckTotal htmlCheckTotal() {
        return new MailpitHtmlCheckTotal(10, 20, 70.0, 20.0, 10.0);
    }

    private static MailpitHtmlCheckWarning htmlCheckWarning() {
        return new MailpitHtmlCheckWarning(
            "css-position",
            "CSS position",
            "Position is not supported",
            "https://example.com/css-position",
            "CSS",
            List.of("css"),
            "position",
            List.of(htmlCheckResult()),
            Map.of("1", "Avoid position"),
            htmlCheckScore()
        );
    }

    private static MailpitLink link() {
        return new MailpitLink("https://example.com", 200, "OK");
    }

    private static MailpitListUnsubscribe listUnsubscribe() {
        return new MailpitListUnsubscribe(
            "<mailto:unsubscribe@example.com>",
            List.of("mailto:unsubscribe@example.com"),
            "none",
            "List-Unsubscribe=One-Click"
        );
    }

    private static MailpitMessage message() {
        return new MailpitMessage(
            "message-1",
            "<message-1@example.com>",
            address(),
            List.of(address()),
            List.of(address()),
            List.of(address()),
            List.of(address()),
            "bounce@example.com",
            "Subject",
            listUnsubscribe(),
            "2026-05-29T12:00:00Z",
            List.of("Important"),
            "user",
            "Plain body",
            "<p>HTML body</p>",
            512,
            List.of(attachment()),
            List.of(attachment())
        );
    }

    private static MailpitMessageRelayConfiguration messageRelayConfiguration() {
        return new MailpitMessageRelayConfiguration(
            true,
            "smtp.example.com:587",
            "bounce@example.com",
            "*@example.com",
            "blocked@example.com",
            "sender@example.com",
            true,
            "*@example.com"
        );
    }

    private static MailpitMessageSummary messageSummary() {
        return new MailpitMessageSummary(
            "message-1",
            "<message-1@example.com>",
            false,
            address(),
            List.of(address()),
            List.of(address()),
            List.of(address()),
            List.of(address()),
            "Subject",
            "2026-05-29T12:00:00Z",
            "user",
            List.of("Important"),
            512,
            1,
            "Plain body"
        );
    }

    private static MailpitRuntimeStats runtimeStats() {
        return new MailpitRuntimeStats(10, 2048, 1, 2, 512, 3, 4);
    }

    private static MailpitSendAddress sendAddress() {
        return new MailpitSendAddress("receiver@example.com", "Receiver");
    }

    private static MailpitSendAttachment sendAttachment() {
        return new MailpitSendAttachment("YXR0YWNobWVudA==", "note.txt", "text/plain", "content-1");
    }

    private static MailpitSendRequest sendRequest() {
        return new MailpitSendRequest(
            sendAddress(),
            List.of(sendAddress()),
            List.of(sendAddress()),
            List.of("bcc@example.com"),
            List.of(sendAddress()),
            "Subject",
            "Plain body",
            "<p>HTML body</p>",
            List.of(sendAttachment()),
            List.of("Important"),
            Map.of("X-Test", "mailpit")
        );
    }

    private static MailpitSpamAssassinRule spamAssassinRule() {
        return new MailpitSpamAssassinRule("Bayesian spam probability", "BAYES_99", 5.5);
    }

    private record SerializationCase(String name, Object value, String expectedJson) {
    }
}
