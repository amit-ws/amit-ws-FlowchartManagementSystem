package com.conceptile.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class FlowChartManagementExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetailResponse> validationExceptionHandler(MethodArgumentNotValidException exp, WebRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        exp.getBindingResult().getAllErrors().forEach(error -> {
            errorMessage.append(error.getDefaultMessage()).append("; ");
        });
        return buildErrorResponse(errorMessage.toString(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorDetailResponse> noDataFoundExceptionHandler(NoDataFoundException exp, WebRequest request) {
        return buildErrorResponse(exp.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(FlowChartMgmtException.class)
    public ResponseEntity<ErrorDetailResponse> flowChartMgmtExceptionHandler(Exception exp, WebRequest request) {
        return buildErrorResponse(exp.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailResponse> globalExceptionHandler(Exception exp, WebRequest request) {
        log.error("Exception class error: {}", exp.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorDetailResponse> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        ErrorDetailResponse errorResponse = ErrorDetailResponse.builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .handler(request.getDescription(false))
                .statusCode(status.value())
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
