package io.micronaut.email.test;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class SpreadsheetUtils {

    public static byte[] spreadsheet() throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        wb.createSheet("Test");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            wb.write(bos);
        } finally {
            bos.close();
        }
        return bos.toByteArray();
    }
}
