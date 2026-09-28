package io.micronaut.email.resend;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.email.AsyncEmailSender;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.EmailSender;
import io.micronaut.email.TransactionalEmailSender;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "resend.api-key", value = "re_xxx")
@MicronautTest(startApplication = false)
class ResendEmailSenderNamedTest {

    @Inject
    BeanContext beanContext;

    @Test
    void sendersAreNamedResend() {
        assertTrue(beanContext.containsBean(ResendEmailSender.class));
        assertTrue(beanContext.containsBean(TransactionalEmailSender.class, Qualifiers.byName("resend")));
        assertTrue(beanContext.containsBean(AsyncTransactionalEmailSender.class, Qualifiers.byName("resend")));
        assertTrue(beanContext.containsBean(EmailSender.class, Qualifiers.byName("resend")));
        assertTrue(beanContext.containsBean(AsyncEmailSender.class, Qualifiers.byName("resend")));
        assertEquals("resend", beanContext.getBean(ResendEmailSender.class).getName());
    }
}
