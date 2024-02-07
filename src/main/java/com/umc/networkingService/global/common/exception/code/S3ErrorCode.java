package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum S3ErrorCode implements ErrorCodeInterface {
    // Image
    FAILED_UPLOAD_S3_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE001", "이미지 저장에 실패하였습니다."),

    // File
    FAILED_UPLOAD_S3_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "FILE001", "파일 저장에 실패하였습니다."),
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
