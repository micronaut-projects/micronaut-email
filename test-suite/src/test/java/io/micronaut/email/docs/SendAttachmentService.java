package io.micronaut.email.docs;

import io.micronaut.email.Attachment;
import io.micronaut.email.BodyType;
import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import io.micronaut.email.test.SpreadsheetUtils;
import io.micronaut.http.MediaType;
import jakarta.inject.Singleton;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Singleton
public class SendAttachmentService {
    private final EmailSender<?, ?> emailSender;

    public SendAttachmentService(EmailSender<?, ?> emailSender) {
        this.emailSender = emailSender;
    }

    public void sendReportText() throws IOException {
        emailSender.send(Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Monthly reports")
                .body("Attached Monthly reports")
                .attachment(Attachment.builder()
                        .filename("reports.xlsx")
                        .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        .content(excel())
                        .build()));
    }

    public void sendReportHtml() throws IOException {
        emailSender.send(Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Monthly reports")
                .body("<html><body><strong>Attached Monthly reports</strong>.</body></html>", BodyType.HTML)
                .attachment(Attachment.builder()
                        .filename("reports.xlsx")
                        .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        .content(excel())
                        .build()));
    }

    private static byte[] excel() throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        wb.createSheet("Reports");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            wb.write(bos);
        } finally {
            bos.close();
        }
        return bos.toByteArray();
    }
}
