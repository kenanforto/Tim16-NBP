package com.hotel.Hotel.controllers;

import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.hotel.Hotel.service.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/download-pdf")
public class ReportController {

    private final PDFService pdfService;

    @GetMapping
    public ResponseEntity<InputStreamResource> generatePdf() throws IOException {
        try {
            byte[] pdfReport = pdfService.generateRoomsBookedByTypePDFReport();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfReport);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=hotel_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(byteArrayInputStream));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
