package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProjectErrorCode implements ErrorCodeInterface {
    EMPTY_PROJECT(HttpStatus.BAD_REQUEST, "PROJECT001", "존재하지 않는 프로젝트입니다."),
    EMPTY_PROJECT_IMAGE(HttpStatus.BAD_REQUEST, "PROJECT002", "프로젝트 이미지는 필수입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
