package io.micronaut.email.docs

import io.micronaut.email.template.Email
import io.micronaut.email.template.EmailTemplateSender
import io.micronaut.views.ModelAndView
import jakarta.inject.Singleton

@Singleton
class WelcomeWithTemplateService(private val emailTemplateSender: EmailTemplateSender<Map<String, String>, Map<String, String>>) {
    fun sendWelcomeEmail() {
        val model = mapOf(
            "message" to "Hello dear Micronaut user",
            "copyright" to "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED",
            "address" to "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141"
        )
        val builder = Email.builder<Map<String, String>, Map<String, String>>()
        emailTemplateSender.send(
            builder
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .text(ModelAndView("texttemplate", model))
                .html(ModelAndView("htmltemplate", model))
                .build()
        )
    }
}