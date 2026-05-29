package io.micronaut.email.mailpit.client.model;

import io.micronaut.context.BeanContext;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@MicronautTest(startApplication = false)
class MailpitSerdeableRecordsTest {

    private static final List<Class<?>> OTHER_SERDEABLE_RECORDS = List.of(
        MailpitAppInformation.class,
        MailpitAddress.class,
        MailpitAttachment.class,
        MailpitAttachmentChecksums.class,
        MailpitChaosTrigger.class,
        MailpitChaosTriggers.class,
        MailpitDeleteMessagesRequest.class,
        MailpitHtmlCheckResponse.class,
        MailpitHtmlCheckResult.class,
        MailpitHtmlCheckScore.class,
        MailpitHtmlCheckTotal.class,
        MailpitHtmlCheckWarning.class,
        MailpitLink.class,
        MailpitLinkCheckResponse.class,
        MailpitListUnsubscribe.class,
        MailpitMessage.class,
        MailpitMessageRelayConfiguration.class,
        MailpitMessagesSummary.class,
        MailpitMessageSummary.class,
        MailpitReleaseMessageRequest.class,
        MailpitRenameTagRequest.class,
        MailpitRuntimeStats.class,
        MailpitSendAddress.class,
        MailpitSendAttachment.class,
        MailpitSendRequest.class,
        MailpitSendResponse.class,
        MailpitSetReadStatusRequest.class,
        MailpitSetTagsRequest.class,
        MailpitSpamAssassinResponse.class,
        MailpitSpamAssassinRule.class,
        MailpitWebUiConfiguration.class
    );

    @Inject
    BeanContext beanContext;

    @TestFactory
    Stream<DynamicTest> otherRecordsAreDeserializable() {
        SerdeIntrospections introspections = introspections();
        return OTHER_SERDEABLE_RECORDS.stream()
            .map(type -> DynamicTest.dynamicTest(type.getSimpleName(), () -> assertDoesNotThrow(
                () -> introspections.getDeserializableIntrospection(Argument.of(type))
            )));
    }

    @TestFactory
    Stream<DynamicTest> otherRecordsAreSerializable() {
        SerdeIntrospections introspections = introspections();
        return OTHER_SERDEABLE_RECORDS.stream()
            .map(type -> DynamicTest.dynamicTest(type.getSimpleName(), () -> assertDoesNotThrow(
                () -> introspections.getSerializableIntrospection(Argument.of(type))
            )));
    }

    private SerdeIntrospections introspections() {
        return assertDoesNotThrow(() -> beanContext.getBean(SerdeIntrospections.class));
    }
}
