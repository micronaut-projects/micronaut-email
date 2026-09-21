from jakarta.inject import Singleton
from java.io import ByteArrayOutputStream
from micronaut.email import Attachment, Email, EmailSender, MultipartBody
from org.apache.poi.xssf.usermodel import XSSFWorkbook


@Singleton
class SendAttachmentService:
    def __init__(self, email_sender: EmailSender):
        self.email_sender = email_sender

    def send_report(self) -> None:
        self.email_sender.send(Email.builder()
                .from_("sender@example.com")
                .to("john@example.com")
                .subject("Monthly reports")
                .body(MultipartBody("<html><body><strong>Attached Monthly reports</strong>.</body></html>", "Attached Monthly reports"))
                .attachment(Attachment.builder()
                        .filename("reports.xlsx")
                        .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        .content(self.excel())
                        .build()))

    @staticmethod
    def excel() -> bytes:
        wb = XSSFWorkbook()
        wb.createSheet("Reports")
        bos = ByteArrayOutputStream()
        try:
            wb.write(bos)
        finally:
            bos.close()
        return bos.toByteArray()
