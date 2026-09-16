package io.micronaut.email.docs.sendgrid

import com.sendgrid.SendGrid
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import jakarta.inject.Singleton

@Requires(property = "spec.name", value = "SendGridBeanCreatedEventListenerTest")
//tag::clazz[]
@Singleton
class SendGridBeanCreatedEventListener implements BeanCreatedEventListener<SendGrid> {

    @Override
    SendGrid onCreated(BeanCreatedEvent<SendGrid> event) {
        SendGrid sendGrid = event.bean
        sendGrid.rateLimitSleep = 5000
        sendGrid
    }
}
//end::clazz[]
