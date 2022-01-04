package io.micronaut.email.javaxemail

import jakarta.inject.Singleton

@Singleton
class GmailMailPropertiesProvider implements MailPropertiesProvider {
    @Override
    Properties mailProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.socketFactory.port","465");
        prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.port","465");
        return prop
    }
}
