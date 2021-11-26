package io.micronaut.email.docs

import io.micronaut.email.Recipient
import io.micronaut.email.Sender
import io.micronaut.email.template.EmailTemplateCourier
import io.micronaut.views.ModelAndView
import jakarta.inject.Singleton

@Singleton
class WelcomeWithTemplateService {
    private final EmailTemplateCourier<?> emailTemplateCourier;

    WelcomeWithTemplateService(EmailTemplateCourier<?> emailTemplateCourier) {
        this.emailTemplateCourier = emailTemplateCourier;
    }

    void sendWelcomeEmail() {
        Map<String, String> model = [message: "Hello dear Micronaut user",
                copyright: "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED",
                address: "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141"]
        emailTemplateCourier.send(new Sender("sender@example.com"),
                new Recipient("john@example.com"),
                "Micronaut test",
                new ModelAndView("texttemplate", model),
                new ModelAndView("htmltemplate", model))
    }
}
