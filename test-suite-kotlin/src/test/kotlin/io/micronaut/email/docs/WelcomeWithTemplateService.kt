package io.micronaut.email.docs

import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.MultipartBody
import io.micronaut.email.template.TemplateBody
import io.micronaut.views.ModelAndView
import jakarta.inject.Singleton

@Singleton
class WelcomeWithTemplateService(private val emailSender: EmailSender<Any, Any>) {

    fun sendWelcomeEmail() {
        val model = mapOf(
            "message" to "Hello dear Micronaut user",
            "copyright" to "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED",
            "address" to "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141"
        )
        emailSender.send(
            Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .body(MultipartBody(
                    TemplateBody(ModelAndView("htmltemplate", model)),
                    TemplateBody(ModelAndView("texttemplate", model))))
        )
    }

}
