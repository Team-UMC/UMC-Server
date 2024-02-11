package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TodayILearnedErrorCode  implements ErrorCodeInterface {
    EMPTY_TODAYILERARNED(HttpStatus.BAD_REQUEST, "TODAYILEANRED001", "존재하지 않는 TIL입니다."),
    NO_PERMISSION_EMPTY_TODAYILERARNED_MEMBER(HttpStatus.BAD_REQUEST, "TODAYILEANRED002", "해당 TIL에 대해 권한이 없는 사용자입니다.");


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
