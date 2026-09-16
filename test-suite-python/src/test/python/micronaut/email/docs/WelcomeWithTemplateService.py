from jakarta.inject import Singleton
from micronaut.email import BodyType, Email, EmailSender, MultipartBody
from micronaut.email.template import TemplateBody
from micronaut.views import ModelAndView


@Singleton
class WelcomeWithTemplateService:
    def __init__(self, email_sender: EmailSender):
        self.email_sender = email_sender

    def send_welcome_email(self) -> None:
        model = {
            "message": "Hello dear Micronaut user",
            "copyright": "© 2021 MICRONAUT FOUNDATION. ALL RIGHTS RESERVED",
            "address": "12140 Woodcrest Executive Dr., Ste 300 St. Louis, MO 63141",
        }
        self.email_sender.send(Email.builder()
                .from_("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .body(MultipartBody(
                        TemplateBody(BodyType.HTML, ModelAndView("htmltemplate", model)),
                        TemplateBody(BodyType.TEXT, ModelAndView("texttemplate", model)))))
