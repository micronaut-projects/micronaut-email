from com.sendgrid import SendGrid
from jakarta.inject import Singleton
from micronaut.context.annotation import Requires
from micronaut.context.event import BeanCreatedEvent, BeanCreatedEventListener


@Requires(property="spec.name", value="SendGridBeanCreatedEventListenerTest")
# tag::clazz[]
@Singleton
class SendGridBeanCreatedEventListener(BeanCreatedEventListener[SendGrid]):

    def onCreated(self, event: BeanCreatedEvent[SendGrid]) -> SendGrid:
        send_grid = event.getBean()
        send_grid.setRateLimitSleep(5000)
        return send_grid
# end::clazz[]
