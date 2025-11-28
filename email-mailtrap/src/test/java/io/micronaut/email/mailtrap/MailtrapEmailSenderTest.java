package io.micronaut.email.mailtrap;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import io.mailtrap.model.response.emails.SendResponse;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailtrapEmailSenderTest {

    @Mock
    private MailtrapClient client;

    @Mock
    private MailtrapEmailComposer composer;

    private MailtrapEmailSender emailSender;

    @BeforeEach
    void setUp() {
        emailSender = new MailtrapEmailSender(client, composer);
    }

    @Test
    void testGetName() {
        assertEquals("mailtrap", emailSender.getName());
    }

    @Test
    void testSendEmailSuccess() throws EmailException {
        String senderEmail = "sender@example.com";
        String recipientEmail = "recipient@example.com";
        String subject = "Test Subject";
        String bodyText = "Test Body";
        String messageId = UUID.randomUUID().toString();

        Email email = Email.builder()
                .from(senderEmail)
                .to(recipientEmail)
                .subject(subject)
                .body(bodyText)
                .build();

        MailtrapMail.MailtrapMailBuilder mailBuilder = mock(MailtrapMail.MailtrapMailBuilder.class);
        MailtrapMail mail = MailtrapMail.builder()
                .from(new Address(senderEmail))
                .to(List.of(new Address(recipientEmail)))
                .subject(subject)
                .text(bodyText)
                .build();

        when(mailBuilder.build()).thenReturn(mail);
        when(composer.compose(eq(email), any())).thenReturn(mailBuilder);

        SendResponse mockResponse = mock(SendResponse.class);
        when(mockResponse.isSuccess()).thenReturn(true);
        when(mockResponse.getMessageIds()).thenReturn(List.of(messageId));
        when(client.send(mail)).thenReturn(mockResponse);

        SendResponse result = emailSender.send(email, builder -> {});

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(List.of(messageId), result.getMessageIds());

        verify(composer).compose(eq(email), any());
        verify(client).send(mail);
        verify(mailBuilder).build();
    }

    @Test
    void testSendEmailWithCustomizations() throws EmailException {
        String senderEmail = "sender@example.com";
        String recipientEmail = "recipient@example.com";
        String subject = "Test Subject";
        String bodyText = "Test Body";

        Email email = Email.builder()
                .from(senderEmail)
                .to(recipientEmail)
                .subject(subject)
                .body(bodyText)
                .build();

        MailtrapMail.MailtrapMailBuilder mailBuilder = mock(MailtrapMail.MailtrapMailBuilder.class);
        MailtrapMail mail = MailtrapMail.builder()
                .from(new Address(senderEmail))
                .to(List.of(new Address(recipientEmail)))
                .subject(subject)
                .text(bodyText)
                .build();

        when(mailBuilder.build()).thenReturn(mail);
        when(composer.compose(eq(email), any())).thenReturn(mailBuilder);

        SendResponse mockResponse = mock(SendResponse.class);
        when(mockResponse.isSuccess()).thenReturn(false);
        when(client.send(mail)).thenReturn(mockResponse);

        SendResponse result = emailSender.send(email, builder -> 
                builder.category("test-category"));

        assertNotNull(result);
        assertFalse(result.isSuccess());

        verify(composer).compose(eq(email), any());
        verify(client).send(mail);
    }

    @Test
    void testSendEmailClientThrowsException() {
        String senderEmail = "sender@example.com";
        String recipientEmail = "recipient@example.com";

        Email email = Email.builder()
                .from(senderEmail)
                .to(recipientEmail)
                .subject("Test")
                .body("Body")
                .build();

        MailtrapMail.MailtrapMailBuilder mailBuilder = mock(MailtrapMail.MailtrapMailBuilder.class);
        MailtrapMail mail = mock(MailtrapMail.class);

        when(mailBuilder.build()).thenReturn(mail);
        when(composer.compose(eq(email), any())).thenReturn(mailBuilder);
        when(client.send(mail)).thenThrow(new RuntimeException("Network error"));

        assertThrows(RuntimeException.class, () -> 
                emailSender.send(email, builder -> {}));

        verify(composer).compose(eq(email), any());
        verify(client).send(mail);
    }
}