package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MascotErrorCode implements ErrorCodeInterface {

    EMPTY_MASCOT(HttpStatus.BAD_REQUEST, "MASCOT001", "존재하지 않는 마스코트입니다."),
    EMPTY_MASCOT_LEVEL(HttpStatus.BAD_REQUEST, "MASCOT002", "해당 레벨의 마스코트가 존재하지 않습니다.")
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
