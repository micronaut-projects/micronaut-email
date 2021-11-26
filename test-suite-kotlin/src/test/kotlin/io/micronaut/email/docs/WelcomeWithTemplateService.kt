package io.micronaut.email.docs

import io.micronaut.email.EmailHeader
import io.micronaut.email.template.EmailTemplateSender
import io.micronaut.views.ModelAndView
import jakarta.inject.Singleton

@Singleton
class WelcomeWithTemplateService(private val emailTemplateSender: EmailTemplateSender<Any?>) {
    fun sendWelcomeEmail() {
        val model = mapOf("message" to "Hello dear Micronaut user",
            "copyright" to "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED",
            "address" to "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141")
        emailTemplateSender.send(
            EmailHeader.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .build(),
            ModelAndView("texttemplate", model),
            ModelAndView("htmltemplate", model)
        )
    }
}