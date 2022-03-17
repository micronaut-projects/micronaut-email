package io.micronaut.email.javamail

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.Introspected
import io.micronaut.email.Attachment
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.test.SpreadsheetUtils
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.netty.handler.codec.http.HttpHeaderNames
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

class JavaMailBodyAndAttachmentSpec extends Specification {

    @AutoCleanup
    GenericContainer<?> mailHog = new GenericContainer<>(DockerImageName.parse("mailhog/mailhog"))
            .withExposedPorts(1025, 8025)
            .waitingFor(Wait.forHttp("/").forPort(8025))

    @AutoCleanup
    ApplicationContext applicationContext

    private PollingConditions conditions = new PollingConditions()

    def setup() {
        mailHog.start()
        applicationContext = ApplicationContext.run([
                "spec.name"          : "JavaxMailEmailSenderAttachmentSpec",
                "mailhog.uri"        : "http://${mailHog.getContainerIpAddress()}:${mailHog.getMappedPort(8025)}/",
                'javamail.properties': ['mail.smtp.host': mailHog.getContainerIpAddress(),
                                        "mail.smtp.port": mailHog.getMappedPort(1025)]])
    }

    def "Can send an email with alternate bodies and attachments"() {
        given:
        EmailSender emailSender = applicationContext.getBean(EmailSender)
        MailhogClient client = applicationContext.getBean(MailhogClient)

        and:
        String from = "sender@here.com"
        String to = "receiver@here.com"
        String subject = "[Javax Mail] Attachment Test" + UUID.randomUUID().toString()
        String html = "<h1>Hola Mundo</h1>"
        String text = "Hello world"
        String filename = "monthlyreports.xlsx"
        String filename2 = "weeklyreports.xlsx"

        when:
        emailSender.send(
                Email.builder()
                        .from(from)
                        .to(to)
                        .subject(subject)
                        .body(html, text)
                        .attachment(createSpreadsheetAttachment(filename, MediaType.MICROSOFT_EXCEL_OPEN_XML))
                        .attachment(createSpreadsheetAttachment(filename2, MediaType.APPLICATION_OCTET_STREAM))
        )

        then:
        conditions.eventually {
            with(client.messages().items[0]) {
                content.headers.Subject == subject
                content.headers.From == from
                content.headers.To == to

                with(content.decoded()) {
                    // Alternative body messages come first
                    with(getBodyPart(0)) {
                        contentType.startsWith('multipart/alternative')
                        content.parts.find { it.contentType.startsWith(MediaType.TEXT_PLAIN) }.content == text
                        content.parts.find { it.contentType.startsWith(MediaType.TEXT_HTML) }.content == html
                    }
                    // Then the attachment(s)
                    with(getBodyPart(1)) {
                        contentType.startsWith(MediaType.MICROSOFT_EXCEL_OPEN_XML)
                        getHeader(HttpHeaderNames.CONTENT_DISPOSITION.toString()).head() == "attachment; filename=$filename"
                    }
                    with(getBodyPart(2)) {
                        contentType.startsWith(MediaType.APPLICATION_OCTET_STREAM)
                        getHeader(HttpHeaderNames.CONTENT_DISPOSITION.toString()).head() == "attachment; filename=$filename2"
                    }
                }
            }
        }
    }

    private Attachment createSpreadsheetAttachment(String filename, String mediaType) {
        Attachment.builder()
                .filename(filename)
                .contentType(mediaType)
                .content(SpreadsheetUtils.spreadsheet())
                .build()
    }

    @Client('${mailhog.uri}')
    static interface MailhogClient {

        @Get("/api/v2/messages")
        MessageResponse messages()
    }

    @Introspected
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    static class MessageResponse {

        int total
        int count
        int start
        List<Item> items
    }

    @Introspected
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Item {
        @JsonProperty("Content")
        Content content
    }

    @Introspected
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Content {
        @JsonProperty("Headers")
        Map<String, String> headers

        @JsonProperty("Body")
        String body

        MimeMultipart decoded() {
            new MimeMultipart(new ByteArrayDataSource(body, headers.'Content-Type'))
        }
    }
}
