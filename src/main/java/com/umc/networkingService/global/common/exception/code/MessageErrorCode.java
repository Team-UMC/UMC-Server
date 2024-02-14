package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MessageErrorCode implements ErrorCodeInterface {

    NOT_FOUND_MESSAGE(HttpStatus.BAD_REQUEST, "MESSAGE001", "존재하지 않는 쪽지입니다."),
    NOT_FOUND_MESSAGE_ROOM(HttpStatus.BAD_REQUEST, "MESSAGE002", "존재하지 않는 쪽지방입니다."),
    ALREADY_EXIST_MESSAGE_ROOM(HttpStatus.BAD_REQUEST, "MESSAGE003", "이미 존재하는 쪽지방입니다."),
    EMPTY_MESSAGE(HttpStatus.BAD_REQUEST, "MESSAGE004", "메시지가 비었습니다."),
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
