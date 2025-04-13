package tn.esprit.services;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class InvoiceService {

    public byte[] generateInvoice(String studentName, String trainingName, double amountPaid, String transactionId, String paymentDate) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // ✅ **Ajout du logo**
            String logoPath = "src/main/resources/static/cod.jpg"; // Mets ton logo ici
            File logoFile = new File(logoPath);
            if (logoFile.exists()) {
                ImageData imageData = ImageDataFactory.create(logoPath);
                Image logo = new Image(imageData).setHeight(50).setAutoScale(true);
                logo.setTextAlignment(TextAlignment.CENTER);
                document.add(logo);
            }

            // ✅ **Titre de la facture**
            Paragraph title = new Paragraph("Training Payment Invoice")
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(new DeviceRgb(255, 165, 0)); // ORANGE
            document.add(title);

            document.add(new Paragraph("\n"));

            // ✅ **Tableau des informations**
            Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            table.setMarginTop(10);
            table.setBackgroundColor(new DeviceRgb(240, 240, 240));

            table.addCell(getStyledCell("👤 Student Name:"));
            table.addCell(getStyledCell(studentName));
            table.addCell(getStyledCell("📚 Training Name:"));
            table.addCell(getStyledCell(trainingName));
            table.addCell(getStyledCell("💰 Amount Paid:"));
            table.addCell(getStyledCell("$" + amountPaid));
            table.addCell(getStyledCell("🔢 Transaction ID:"));
            table.addCell(getStyledCell(transactionId));
            table.addCell(getStyledCell("📅 Payment Date:"));
            table.addCell(getStyledCell(paymentDate));

            document.add(table);

            document.add(new Paragraph("\nThank you for your payment! 🎓")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(new DeviceRgb(34, 167, 67))
                    .setFontSize(14));

            document.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ✅ **Méthode pour styliser les cellules du tableau**
    private Cell getStyledCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setPadding(5)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT)
                .setBorder(null);
    }
}
