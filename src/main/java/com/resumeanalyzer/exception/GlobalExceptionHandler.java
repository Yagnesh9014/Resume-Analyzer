package com.resumeanalyzer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> handleMissingFile(
            MissingServletRequestPartException e) {

        return ResponseEntity
                .badRequest()
                .body("Please upload a PDF resume.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidInput(
            IllegalArgumentException e) {

        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleLargeFile(
            MaxUploadSizeExceededException e) {

        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("PDF file is too large. Maximum allowed size is 5 MB.");
    }

    @ExceptionHandler(AiAnalysisException.class)
    public ResponseEntity<String> handleAiFailure(
            AiAnalysisException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(
                        "AI analysis service is temporarily unavailable. " +
                                "Please try again later."
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedError(
            Exception e) {

        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        "An unexpected server error occurred. " +
                                "Please try again later."
                );
    }
}