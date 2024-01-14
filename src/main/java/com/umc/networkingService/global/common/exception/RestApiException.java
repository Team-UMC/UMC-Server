package com.umc.networkingService.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private String message;
    private final HttpStatus status;
}
