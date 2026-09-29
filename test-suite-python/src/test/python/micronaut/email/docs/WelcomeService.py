from jakarta.inject import Singleton
from micronaut.email import Email, EmailSender, MultipartBody


@Singleton
class WelcomeService:
    def __init__(self, email_sender: EmailSender):
        self.email_sender = email_sender

    def send_welcome_email(self) -> None:
        self.email_sender.send(Email.builder()
                .from_("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .body(MultipartBody("<html><body><strong>Hello</strong> dear Micronaut user.</body></html>", "Hello dear Micronaut user")))
