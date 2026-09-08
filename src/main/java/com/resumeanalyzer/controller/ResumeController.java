package com.resumeanalyzer.controller;

import com.resumeanalyzer.service.AiService;
import com.resumeanalyzer.service.PdfService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final PdfService pdfService;
    private final AiService aiService;

    public ResumeController(PdfService pdfService, AiService aiService) {
        this.pdfService = pdfService;
        this.aiService = aiService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(
            @RequestParam("file") MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Please upload a PDF resume.");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null ||
                !fileName.toLowerCase().endsWith(".pdf")) {

            return ResponseEntity
                    .badRequest()
                    .body("Only PDF files are supported.");
        }

        String resumeText =
                pdfService.extractText(file.getBytes());

        if (resumeText == null || resumeText.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body("Unable to extract readable text from this PDF.");
        }

        String analysis =
                aiService.analyzeResume(resumeText);

        if (analysis == null || analysis.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body("AI analysis returned an empty response.");
        }

        return ResponseEntity.ok(analysis);
    }
}