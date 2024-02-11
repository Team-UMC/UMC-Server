package com.umc.networkingService.global.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorCode {
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}