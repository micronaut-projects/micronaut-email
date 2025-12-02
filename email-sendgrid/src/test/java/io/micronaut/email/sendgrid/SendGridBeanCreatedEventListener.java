package io.micronaut.email.sendgrid;

import com.sendgrid.SendGrid;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import org.jspecify.annotations.NonNull;
import jakarta.inject.Singleton;

@Requires(property = "spec.name", value = "SendGridSpec")
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
