package io.micronaut.email.docs

import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.MultipartBody
import io.micronaut.email.template.TemplateBody
import io.micronaut.views.ModelAndView
import jakarta.inject.Singleton

@Singleton
class WelcomeWithTemplateService {
    private final EmailSender<?, ?> emailSender

    WelcomeWithTemplateService(EmailSender<?, ?> emailSender) {
        this.emailSender = emailSender
    }

    void sendWelcomeEmail() {
        Map<String, String> model = [message: "Hello dear Micronaut user",
                copyright: "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED",
                address: "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141"]
        emailSender.send(Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .body(new MultipartBody(
                        new TemplateBody(new ModelAndView<>("htmltemplate", model)),
                        new TemplateBody(new ModelAndView<>("texttemplate", model)))))
    }
}
