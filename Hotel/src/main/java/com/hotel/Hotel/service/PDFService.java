package com.hotel.Hotel.service;

import com.hotel.Hotel.models.Report;
import com.hotel.Hotel.repository.ReportRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PDFService {

    @Autowired
    private ReportRepository reportRepository;

    public byte[] generateRoomsBookedByTypePDFReport() throws DocumentException {
        List<Report> reports = reportRepository.findAll();

        Map<String, Double> roomTypeSums = reports.stream()
                .collect(Collectors.groupingBy(
                        Report::getRoomType,
                        Collectors.summingDouble(Report::getRoomPrice)
                ));

        double totalRevenue = roomTypeSums.values().stream().mapToDouble(Double::doubleValue).sum();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        Paragraph title = new Paragraph("Izvjestaj hotela", new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        Paragraph totalRevenueParagraph = new Paragraph("UKUPNA ZARADA HOTELA: " + totalRevenue + " KM",
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        totalRevenueParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(totalRevenueParagraph);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("TIP SOBE");
        table.addCell("ZARADA");

        for (Map.Entry<String, Double> entry : roomTypeSums.entrySet()) {
            table.addCell(entry.getKey());
            table.addCell(String.format("%.2f", entry.getValue()));
        }

        document.add(table);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

}
