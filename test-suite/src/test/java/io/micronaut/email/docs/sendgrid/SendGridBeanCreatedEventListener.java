package io.micronaut.email.docs.sendgrid;

import com.sendgrid.SendGrid;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;

@Requires(property = "spec.name", value = "SendGridBeanCreatedEventListenerTest")
//tag::clazz[]
@Singleton
class SendGridBeanCreatedEventListener implements BeanCreatedEventListener<SendGrid> {

    @Override
    public SendGrid onCreated(@NonNull BeanCreatedEvent<SendGrid> event) {
        SendGrid sendGrid = event.getBean();
        sendGrid.setRateLimitSleep(5000);
        return sendGrid;
    }
}
//end::clazz[]
