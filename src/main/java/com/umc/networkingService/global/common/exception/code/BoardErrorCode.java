package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardErrorCode implements ErrorCodeInterface {

    BAD_REQUEST_BOARD(HttpStatus.BAD_REQUEST,"BOARD001", "금지된 요청입니다."),
    EMPTY_BOARD(HttpStatus.NOT_FOUND, "BOARD002", "게시글을 찾을 수 없습니다."),
    NO_AUTHORIZATION_BOARD(HttpStatus.BAD_REQUEST,"BOARD003", "해당 게시판 API에 대한 권한이 없습니다."),
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
