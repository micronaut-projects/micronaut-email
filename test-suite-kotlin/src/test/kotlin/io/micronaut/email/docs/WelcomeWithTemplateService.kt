package io.micronaut.email.docs

import io.micronaut.email.Recipient
import io.micronaut.email.Sender
import io.micronaut.email.template.EmailTemplateCourier
import io.micronaut.views.ModelAndView
import jakarta.inject.Singleton

@Singleton
class WelcomeWithTemplateService(private val emailTemplateCourier: EmailTemplateCourier<Any?>) {
    fun sendWelcomeEmail() {
        val model = mapOf("message" to "Hello dear Micronaut user",
            "copyright" to "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED",
            "address" to "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141")
        emailTemplateCourier.send(
            Sender("sender@example.com"),
            Recipient("john@example.com"),
            "Micronaut test",
            ModelAndView("texttemplate", model),
            ModelAndView("htmltemplate", model)
        )
    }
}