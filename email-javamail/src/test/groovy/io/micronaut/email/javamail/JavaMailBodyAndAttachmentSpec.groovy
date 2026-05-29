package io.micronaut.email.javamail

import io.micronaut.context.ApplicationContext
import io.micronaut.email.Attachment
import io.micronaut.email.Contact
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.mailpit.client.MailpitClient
import io.micronaut.email.mailpit.client.model.MailpitAddress
import io.micronaut.email.mailpit.client.model.MailpitDeleteMessagesRequest
import io.micronaut.email.mailpit.client.model.MailpitMessage
import io.micronaut.email.mailpit.client.model.MailpitMessageSummary
import io.micronaut.email.test.Mailpit
import io.micronaut.email.test.SpreadsheetUtils
import io.micronaut.http.HttpHeaders
import io.micronaut.http.MediaType
import jakarta.mail.Session
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart
import org.testcontainers.DockerClientFactory
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class JavaMailBodyAndAttachmentSpec extends Specification {
    @AutoCleanup
    ApplicationContext applicationContext

    private PollingConditions conditions = new PollingConditions()

    def setup() {
        applicationContext = ApplicationContext.run([
                "spec.name"          : "JavaxMailEmailSenderAttachmentSpec",
                'javamail.properties': Mailpit.getJavaMailProperties(),
        ] + Mailpit.getProperties())
        applicationContext.getBean(MailpitClient).deleteMessages(new MailpitDeleteMessagesRequest([]))
    }

    @spock.lang.Requires({ DockerClientFactory.instance().isDockerAvailable() })
    def "can send an email from #desc contact details"(Object from, String expectedFrom, String desc) {
        given:
        EmailSender emailSender = applicationContext.getBean(EmailSender)
        MailpitClient client = applicationContext.getBean(MailpitClient)

        and:
        Contact namedContact = new Contact("receiver@here.com", "Kif")
        Contact unnamedContact = new Contact("noname@here.com")
        String stringContact = "someone@here.com"
        String subject = "[Javax Mail] Test" + UUID.randomUUID().toString()


        when:
        Email.Builder emailBuilder = Email.builder()
        if (from instanceof String) {
            emailBuilder.from((String) from)
        } else if (from instanceof Contact) {
            emailBuilder.from((Contact) from)
        }
        emailSender.send(
                emailBuilder
                        .to(namedContact).to(unnamedContact).to(stringContact)
                        .cc(unnamedContact).cc(stringContact).cc(namedContact)
                        .subject(subject)
                        .body("Hiya!")
        )

        then:
        conditions.eventually {
            MailpitMessage message = client.getMessage(client.listMessages(0, 1).messages()[0].id())
            message.subject() == subject
            formatAddress(message.from()) == expectedFrom
            message.to().collect { formatAddress(it) }.join(', ') == 'Kif <receiver@here.com>, noname@here.com, someone@here.com'
            message.cc().collect { formatAddress(it) }.join(', ') == 'noname@here.com, someone@here.com, Kif <receiver@here.com>'
        }

        where:
        from                                   | expectedFrom             | desc
        new Contact("sarah@here.com", "Sarah") | 'Sarah <sarah@here.com>' | 'named'
        new Contact("tim@here.com")            | 'tim@here.com'           | 'unnamed'
        "plain@email.com"                      | 'plain@email.com'        | 'string'
    }

    @spock.lang.Requires({ DockerClientFactory.instance().isDockerAvailable() })
    def "Can send an email with alternate bodies and attachments"() {
        given:
        EmailSender emailSender = applicationContext.getBean(EmailSender)
        MailpitClient client = applicationContext.getBean(MailpitClient)

        and:
        Contact from = new Contact("sender@here.com", "Zapp Brannigan")
        Contact to = new Contact("receiver@here.com", "Kif")
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
            MailpitMessageSummary summary = client.listMessages(0, 1).messages()[0]
            MailpitMessage message = client.getMessage(summary.id())
            message.subject() == subject
            formatAddress(message.from()) == "$from.name <$from.email>"
            message.to().collect { formatAddress(it) }.join(', ') == "$to.name <$to.email>"
            message.text() == "Hello world"
            message.html() == "<h1>Hola Mundo</h1>"
            message.attachments().collect { it.fileName() } == [filename, filename2]
            message.attachments().collect { it.contentType() } == [MediaType.MICROSOFT_EXCEL_OPEN_XML, MediaType.APPLICATION_OCTET_STREAM]

            with(decodeRawMessage(client.getRawMessage(summary.id()))) {
                // Alternative body messages come first
                with(getBodyPart(0)) {
                    contentType.startsWith('multipart/alternative')
                    content.parts.find { it.contentType.startsWith(MediaType.TEXT_PLAIN) }.content == text
                    content.parts.find { it.contentType.startsWith(MediaType.TEXT_HTML) }.content == html
                }
                // Then the attachment(s)
                with(getBodyPart(1)) {
                    contentType.startsWith(MediaType.MICROSOFT_EXCEL_OPEN_XML)
                    getHeader(HttpHeaders.CONTENT_DISPOSITION.toString()).head() == "attachment; filename=$filename"
                }
                with(getBodyPart(2)) {
                    contentType.startsWith(MediaType.APPLICATION_OCTET_STREAM)
                    getHeader(HttpHeaders.CONTENT_DISPOSITION.toString()).head() == "attachment; filename=$filename2"
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

    private static MimeMultipart decodeRawMessage(String rawMessage) {
        new MimeMessage(
                Session.getDefaultInstance(new Properties()),
                new ByteArrayInputStream(rawMessage.getBytes('UTF-8'))
        ).content as MimeMultipart
    }

    private static String formatAddress(MailpitAddress address) {
        address.name() ? "${address.name()} <${address.address()}>" : address.address()
    }
}
