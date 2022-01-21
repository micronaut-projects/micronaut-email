package io.micronaut.email.docs

import io.micronaut.email.Attachment
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.MultipartBody
import jakarta.inject.Singleton
import org.apache.poi.xssf.usermodel.XSSFWorkbook

@Singleton
class SendAttachmentService {
    private final EmailSender<?, ?> emailSender;

    SendAttachmentService(EmailSender<?, ?> emailSender) {
        this.emailSender = emailSender;
    }

    void sendReport() throws IOException {
        emailSender.send(Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Monthly reports")
                .body(new MultipartBody("<html><body><strong>Attached Monthly reports</strong>.</body></html>", "Attached Monthly reports"))
                .attachment(Attachment.builder()
                        .filename("reports.xlsx")
                        .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        .content(excel())
                        .build()))
    }

    private static byte[] excel() throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook()
        wb.createSheet("Reports")
        ByteArrayOutputStream bos = new ByteArrayOutputStream()
        try {
            wb.write(bos)
        } finally {
            bos.close()
        }
        bos.toByteArray()
    }
}
