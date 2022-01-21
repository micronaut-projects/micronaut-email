package io.micronaut.email.javamail

import io.micronaut.context.ApplicationContext
import io.micronaut.email.javamail.sender.JavaMailConfiguration
import io.micronaut.email.javamail.sender.MailPropertiesProvider
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class MailPropertiesProviderViaConfigurationSpec extends Specification {

    @AutoCleanup
    @Shared
    ApplicationContext applicationContext = ApplicationContext.run([
            'javamail.properties': ['mail.smtp.host': "smtp.gmail.com",
                                    'mail.smtp.socketFactory.port': "465",
                                    'mail.smtp.socketFactory.class': "javax.net.ssl.SSLSocketFactory",
                                    'mail.smtp.auth': "true",
                                    "mail.smtp.port": "465"]])

    void "a bean of type MailPropertiesProvider is registered via configuration"() {
        expect:
        applicationContext.containsBean(JavaMailConfiguration)
        applicationContext.containsBean(MailPropertiesProvider)

        when:
        MailPropertiesProvider mailPropertiesProvider = applicationContext.getBean(MailPropertiesProvider)
        Properties properties = mailPropertiesProvider.mailProperties()

        then:
        properties
        "smtp.gmail.com" == properties.get("mail.smtp.host")
        "465" == properties.get("mail.smtp.socketFactory.port")
        "javax.net.ssl.SSLSocketFactory" == properties.get("mail.smtp.socketFactory.class")
        "true" == properties.get("mail.smtp.auth")
        "465" == properties.get("mail.smtp.port")
    }

}
