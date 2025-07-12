package com.example.pohwreport;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class PdfMergeController {

    @GetMapping("/api/pdf/merge")
    public ResponseEntity<byte[]> mergeStoredPdfs(
            @RequestParam(required = false, defaultValue = "11888") String empCode) throws IOException {
        try {
            // 1. Generate the employee report PDF
            ReportGeneratorTest reportGenerator = new ReportGeneratorTest();
            String generatedReportPath = reportGenerator.main(empCode); // Generates PDF report

            String folderPath = "/root/pohw/demand/";
            String fileName2 = empCode + ".pdf";

            // Retry logic to wait for file1 to appear
            int retries = 0;
            File file1 = new File(generatedReportPath);
            while (!file1.exists() && retries < 10) {
                Thread.sleep(1000); // Wait 1 second per retry
                retries++;
            }
            if (!file1.exists()) {
                throw new IOException("Generated report not found after waiting.");
            }

            File file2 = new File(folderPath, fileName2);

            System.out.println("Looking for: " + generatedReportPath);
            System.out.println("file1 exists? " + file1.exists());
            System.out.println("file2 exists? " + file2.exists());

            // 2. Merge PDFs using file paths (no streams)
            PDFMergerUtility pdfMerger = new PDFMergerUtility();
            ByteArrayOutputStream mergedPdfOutputStream = new ByteArrayOutputStream();

            pdfMerger.addSource(file1);
            if (file2.exists()) {
                pdfMerger.addSource(file2);
                System.out.println("Merging both PDFs.");
            } else {
                System.out.println("Second file not found, merging only the generated report.");
            }

            pdfMerger.setDestinationStream(mergedPdfOutputStream);
            pdfMerger.mergeDocuments(null);

            byte[] mergedPdfBytes = mergedPdfOutputStream.toByteArray();

            // 3. Clean up - delete the generated report
            Files.deleteIfExists(file1.toPath());

            // 4. Return merged PDF as HTTP response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pohw_"+empCode+".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(mergedPdfBytes);

        } catch (Exception e) {
            throw new IOException("Failed to generate and merge reports: " + e.getMessage(), e);
        }
    }
}




