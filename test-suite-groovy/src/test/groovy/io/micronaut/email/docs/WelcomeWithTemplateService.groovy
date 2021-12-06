package io.micronaut.email.docs

import io.micronaut.core.util.CollectionUtils
import io.micronaut.email.template.Email
import io.micronaut.email.template.EmailTemplateSender
import io.micronaut.views.ModelAndView
import jakarta.inject.Singleton

@Singleton
class WelcomeWithTemplateService {
    private final EmailTemplateSender<Map<String, String>, Map<String, String>> emailTemplateSender

    WelcomeWithTemplateService(EmailTemplateSender<Map<String, String>, Map<String, String>> emailTemplateSender) {
        this.emailTemplateSender = emailTemplateSender
    }

    void sendWelcomeEmail() {
        Map<String, String> model = [message: "Hello dear Micronaut user",
                copyright: "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED",
                address: "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141"]
        Email.Builder<Map<String,String>, Map<String,String>> builder = Email.builder()
        emailTemplateSender.send(builder
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .text(new ModelAndView<>("texttemplate", model))
                .html(new ModelAndView<>("htmltemplate", model))
                .build())
    }
}
