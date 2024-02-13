package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UniversityErrorCode implements ErrorCodeInterface {

    EMPTY_UNIVERSITY(HttpStatus.BAD_REQUEST, "UNIVERSITY001", "존재하지 않는 대학교입니다."),
    DUPLICATE_UNIVERSITY_NAME(HttpStatus.BAD_REQUEST, "UNIVERSITY002", "이미 존재하는 대학교 이름입니다.")
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
