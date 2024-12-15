package com.conceptile.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorDetailResponse {
    String message;
    LocalDateTime timestamp;
    String handler;
    Integer statusCode;
}