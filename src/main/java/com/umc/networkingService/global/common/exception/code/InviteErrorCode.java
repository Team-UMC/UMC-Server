package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum InviteErrorCode implements ErrorCodeInterface {

    EXPIRED_INVITE_CODE(HttpStatus.BAD_REQUEST, "INVITE001", "이미 만료된 초대 코드입니다."),
    UNAUTHORIZED_CREATE_INVITE(HttpStatus.BAD_REQUEST, "INVITE002", "해당 역할에 대해서 초대 코드를 생성할 권한이 없습니다."),
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
