package io.micronaut.email.docs

import io.micronaut.email.Attachment
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import jakarta.inject.Singleton
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream

@Singleton
class SendAttachmentService(private val emailSender: EmailSender<Any>) {
    fun sendWelcomeEmail() {
        emailSender.send(
            Email.builder()
                .from("sender@example.com")
                .to("john@example.com")
                .subject("Monthly reports")
                .text("Attached Monthly reports")
                .html("<html><body><strong>Attached Monthly reports</strong>.</body></html>")
                .attachment(
                    Attachment.builder()
                    .filename("reports.xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .content(excel())
                    .build())
                .build()
        )
    }

    private fun excel(): ByteArray {
        val wb = XSSFWorkbook()
        wb.createSheet("Reports")
        val bos = ByteArrayOutputStream()
        bos.use { byteArrayOutputStream ->
            wb.write(byteArrayOutputStream)
        }
        return bos.toByteArray()
    }
}