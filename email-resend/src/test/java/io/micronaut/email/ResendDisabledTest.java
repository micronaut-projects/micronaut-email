package io.micronaut.email;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.discovery.ServiceInstanceList;
import io.micronaut.email.resend.ResendConfiguration;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Property(name = "resend.enabled", value = StringUtils.FALSE)
@Property(name = "resend.api-key", value = "re_xxx")
@MicronautTest(startApplication = false)
class ResendDisabledTest {

    @Inject
    BeanContext beanContext;

    @Test
    void resendBeansAreNotLoaded() {
        assertFalse(beanContext.containsBean(ResendConfiguration.class));
        assertFalse(beanContext.containsBean(TransactionalEmailSender.class));
        assertFalse(beanContext.containsBean(AsyncTransactionalEmailSender.class));
        assertFalse(beanContext.containsBean(EmailSender.class));
        assertFalse(beanContext.containsBean(AsyncEmailSender.class));
        assertFalse(beanContext.containsBean(ServiceInstanceList.class, Qualifiers.byName("resend")));
    }
}
