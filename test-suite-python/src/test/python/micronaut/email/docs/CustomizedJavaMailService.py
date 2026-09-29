import logging

from jakarta.inject import Singleton
from jakarta.mail import Message, MessagingException
from micronaut.email import Email, EmailSender, MultipartBody

LOG = logging.getLogger(__name__)


@Singleton
class CustomizedJavaMailService:
    """An example of customization for JavaMail messages"""

    def __init__(self, email_sender: EmailSender[Message, object]):
        self.email_sender = email_sender

    def send_customized_email(self) -> None:
        email = (Email.builder()
                .from_("sender@example.com")
                .to("john@example.com")
                .subject("Micronaut test")
                .body(
                        MultipartBody(
                                "<html><body><strong>Hello</strong> dear Micronaut user.</body></html>",
                                "Hello dear Micronaut user"
                        )
                ))

        # Customize the message with a header prior to sending
        def customize(message: Message) -> None:
            try:
                message.addHeader("List-Unsubscribe", "<mailto:list@host.com?subject=unsubscribe>")
            except MessagingException as e:
                LOG.exception("Failed to add header")

        self.email_sender.send(email, customize)
