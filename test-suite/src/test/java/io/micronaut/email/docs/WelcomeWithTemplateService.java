package io.micronaut.email.docs;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.template.EmailTemplateSender;
import io.micronaut.email.template.Email;
import io.micronaut.views.ModelAndView;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class WelcomeWithTemplateService {
    private final EmailTemplateSender<Map<String, String>, Map<String, String>> emailTemplateSender;

    public WelcomeWithTemplateService(EmailTemplateSender<Map<String, String>, Map<String, String>> emailTemplateSender) {
        this.emailTemplateSender = emailTemplateSender;
    }

    public void sendWelcomeEmail() {
        Map<String, String> model = CollectionUtils.mapOf("message", "Hello dear Micronaut user",
                "copyright", "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED",
                "address", "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141");
        Email.Builder<Map<String,String>, Map<String,String>> builder = Email.builder();
        emailTemplateSender.send(builder
                        .from("sender@example.com")
                        .to("john@example.com")
                        .subject("Micronaut test")
                        .text(new ModelAndView<>("texttemplate", model))
                        .html(new ModelAndView<>("htmltemplate", model))
                        .build());
    }
}
