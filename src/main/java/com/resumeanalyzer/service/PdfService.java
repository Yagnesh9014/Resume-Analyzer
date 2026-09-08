package com.resumeanalyzer.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfService {

    public String extractText(byte[] pdfBytes) {

        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("PDF file is empty.");
        }

        try {

            ByteArrayResource resource =
                    new ByteArrayResource(pdfBytes);

            PdfDocumentReaderConfig config =
                    PdfDocumentReaderConfig.builder()
                            .build();

            PagePdfDocumentReader reader =
                    new PagePdfDocumentReader(
                            resource,
                            config
                    );

            List<Document> documents =
                    reader.get();

            if (documents == null || documents.isEmpty()) {
                throw new IllegalArgumentException(
                        "No readable pages found in PDF."
                );
            }

            StringBuilder text =
                    new StringBuilder();

            for (Document document : documents) {

                if (document.getText() != null) {
                    text.append(
                            document.getText()
                    ).append("\n");
                }
            }

            return text.toString().trim();

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Failed to read PDF document.",
                    e
            );
        }
    }
}