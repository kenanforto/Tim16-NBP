package com.hotel.Hotel.service;

import com.hotel.Hotel.models.Report;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PDFService {

    @Autowired
    private Connection jdbcConnection;

    public byte[] generateRoomsBookedByTypePDFReport() throws DocumentException, IOException {
        List<Report> reports = new ArrayList<>();
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_REPORT");
            while (resultSet.next()) {
                Report roomBooked = new Report();
                roomBooked.setId(resultSet.getInt("ID"));
                roomBooked.setRoomType(resultSet.getString("ROOM_TYPE"));
                roomBooked.setRoomPrice(resultSet.getDouble("ROOM_PRICE"));
                reports.add(roomBooked);
            }
        } catch (SQLException e) {
            log.error("Error fetching data from report table", e);
        }
        Map<String, List<Report>> reportsByRoomType = reports.stream()
                .collect(Collectors.groupingBy(Report::getRoomType));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        Paragraph title = new Paragraph("THE BELLMONT", new Font(Font.FontFamily.HELVETICA, 40, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        for (Map.Entry<String, List<Report>> entry : reportsByRoomType.entrySet()) {
            String roomType = entry.getKey();
            List<Report> roomReports = entry.getValue();

            document.add(new Paragraph(roomType.toUpperCase(),
                    new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD)));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.addCell("Tip Sobe");
            table.addCell("Cijena (KM)");

            double totalRoomTypeRevenue = 0;
            for (Report report : roomReports) {
                table.addCell(String.valueOf(report.getRoomType()));
                table.addCell(String.format("%.2f", report.getRoomPrice()));
                totalRoomTypeRevenue += report.getRoomPrice();
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            Paragraph revenue = new Paragraph("Ukupna zarada za " + roomType + ": " + String.format("%.2f", totalRoomTypeRevenue) + " KM",
                    new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            revenue.setAlignment(Element.ALIGN_RIGHT);
            document.add(revenue);
            document.add(Chunk.NEWLINE);
            document.add(new LineSeparator());
            document.add(Chunk.NEWLINE);
        }

        double totalRevenue = reports.stream().mapToDouble(Report::getRoomPrice).sum();
        Paragraph totalRevenueParagraph = new Paragraph("UKUPNA ZARADA HOTELA: " + String.format("%.2f", totalRevenue) + " KM",
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        totalRevenueParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(Chunk.NEWLINE);
        document.add(totalRevenueParagraph);

        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
