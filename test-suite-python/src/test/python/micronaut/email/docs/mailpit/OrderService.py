from typing import Annotated

from jakarta.inject import Singleton
from jakarta.validation.constraints import Email as EmailAddress, NotBlank
from micronaut.context.annotation import Requires
from micronaut.email import Email, EmailSender


@Requires(property="spec.name", value="OrderServiceTest")
@Singleton
class OrderService:
    def __init__(self, email_sender: EmailSender):
        self.email_sender = email_sender

    def send_order_email(self, recipient: Annotated[str, EmailAddress],
                         order_number: Annotated[str, NotBlank]) -> None:
        text = "We have received your order " + order_number + ". You will receive your product soon."
        html = "<html><body><p>" + text + "</p></body></html>"
        self.email_sender.send(Email.builder()
                .to(recipient)
                .subject("Order Number: " + order_number)
                .body(html, text))
