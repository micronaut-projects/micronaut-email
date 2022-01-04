package io.micronaut.email.javaxemail;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Requires(condition = GmailCondition.class)
@Singleton
public class GmailSessionProvider implements SessionProvider {
    private final Properties properties;

    public GmailSessionProvider(MailPropertiesProvider mailPropertiesProvider) {
        this.properties = mailPropertiesProvider.mailProperties();
    }

    @Override
    @NonNull
    public Session session() {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(System.getenv("GMAIL_USERNAME"), System.getenv("GMAIL_PASSWORD"));
            }
        });
    }
}
