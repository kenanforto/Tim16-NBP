package com.hotel.Hotel.controllers;

import com.hotel.Hotel.models.Report;
import com.hotel.Hotel.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import com.hotel.Hotel.service.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/download-pdf")
public class ReportController {

    private final ReportRepository reportRepository; // koristit cemo kasnije, kada napravimo proceduru
    private final PDFService pdfService;

    @GetMapping
    public ResponseEntity<byte[]> generatePdf() throws IOException {
        List<Report> reports = List.of( // popraviti
                new Report(
                        1,
                        "Nejla",
                        1.0,
                        null
                )
        );
        byte[] pdf = pdfService.generatePdf(reports);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
