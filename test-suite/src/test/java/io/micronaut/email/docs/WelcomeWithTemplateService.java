package io.micronaut.email.docs;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.EmailHeader;
import io.micronaut.email.template.EmailTemplateSender;
import io.micronaut.views.ModelAndView;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class WelcomeWithTemplateService {
    private final EmailTemplateSender<?> emailTemplateSender;

    public WelcomeWithTemplateService(EmailTemplateSender<?> emailTemplateSender) {
        this.emailTemplateSender = emailTemplateSender;
    }

    public void sendWelcomeEmail() {
        Map<?, ?> model = CollectionUtils.mapOf("message", "Hello dear Micronaut user",
                "copyright", "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED",
                "address", "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141");
        emailTemplateSender.send(EmailHeader.builder()
                        .from("sender@example.com")
                        .to("john@example.com")
                        .subject("Micronaut test")
                        .build(),
                new ModelAndView("texttemplate", model),
                new ModelAndView("htmltemplate", model));
    }
}
