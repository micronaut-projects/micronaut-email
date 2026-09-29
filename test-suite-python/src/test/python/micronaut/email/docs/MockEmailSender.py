from jakarta.inject import Named, Singleton
from java.util.function import Consumer
from micronaut.context.annotation import Requires
from micronaut.email import Email, TransactionalEmailSender


@Requires(property="mock.emailsender", value="true")
@Named("mock")
@Singleton
class MockEmailSender(TransactionalEmailSender[object, Email]):

    def __init__(self) -> None:
        self.emails: list[Email] = []
        self.requests: list[Consumer[object]] = []

    def get_emails(self) -> list[Email]:
        return self.emails

    def get_requests(self) -> list[Consumer[object]]:
        return self.requests

    def getName(self) -> str:
        return "mock"

    def send(self, email: Email, emailRequest: Consumer[object]) -> Email:
        self.emails.append(email)
        self.requests.append(emailRequest)
        return email
