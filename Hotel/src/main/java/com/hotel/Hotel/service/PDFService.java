package com.hotel.Hotel.service;

import com.hotel.Hotel.models.Report;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PDFService {

    public byte[] generatePdf(List<Report> reports) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
                stream.beginText();
                stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                stream.setLeading(14.5f);
                stream.newLineAtOffset(50, 750);

                for (Report report : reports) {
                    stream.showText("Room: " + report.getRoomType());
                    stream.newLine();
                    stream.showText("Price: " + report.getRoomPrice());
                    stream.newLine();
                    stream.showText("Booking date: " + report.getBookingDate());
                    stream.newLine();
                    stream.newLine();
                }

                stream.endText();
            }

            document.save(out);
            return out.toByteArray();
        }
    }
}
