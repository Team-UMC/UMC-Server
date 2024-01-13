package com.umc.networkingService.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;
}
