package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FriendErrorCode implements ErrorCodeInterface {

    ALREADY_FRIEND_RELATION(HttpStatus.BAD_REQUEST, "FRIEND001", "이미 친구 관계인 사용자입니다."),
    NOT_FRIEND_RELATION(HttpStatus.CONFLICT, "FRIEND002", "친구 관계가 아닌 사용자입니다."),

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
