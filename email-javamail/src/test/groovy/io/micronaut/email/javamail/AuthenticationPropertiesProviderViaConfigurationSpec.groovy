package io.micronaut.email.javamail

import io.micronaut.context.ApplicationContext
import io.micronaut.email.javamail.sender.DefaultSessionProvider
import io.micronaut.email.javamail.sender.authentication.JavaMailAuthenticationConfiguration
import spock.lang.AutoCleanup
import spock.lang.Specification

class AuthenticationPropertiesProviderViaConfigurationSpec extends Specification {

    @AutoCleanup
    ApplicationContext applicationContext = ApplicationContext.run(
            'javamail.properties': [
                    'mail.smtp.host'               : "smtp.gmail.com",
                    'mail.smtp.socketFactory.port' : "465",
                    'mail.smtp.socketFactory.class': "javax.net.ssl.SSLSocketFactory",
                    "mail.smtp.port"               : "465"
            ],
            'javamail.authentication.username': 'my.username',
            'javamail.authentication.password': 'my.password'
    )

    void "a bean of type MailPropertiesProvider is registered via configuration"() {
        expect:
        applicationContext.containsBean(JavaMailAuthenticationConfiguration)
        applicationContext.containsBean(DefaultSessionProvider)

        when:
        def config = applicationContext.getBean(JavaMailAuthenticationConfiguration)

        then:
        config.username == 'my.username'
        config.password == 'my.password'
    }
}
