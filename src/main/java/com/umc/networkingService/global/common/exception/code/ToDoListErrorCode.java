package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ToDoListErrorCode implements ErrorCodeInterface {

    EMPTY_TODOLIST(HttpStatus.BAD_REQUEST, "TODOLIST001", "존재하지 않은 투두리스트입니다."),
    NO_AUTHORIZATION_TODOLIST(HttpStatus.BAD_REQUEST, "TODOLIST002", "해당 투두리스트에 대한 수정 권한이 없습니다."),
    ALREADY_COMPLETE_TODOLIST(HttpStatus.BAD_REQUEST, "TODOLIST003", "이미 완료된 투두리스트입니다."),
    EXPIRED_TODOLIST(HttpStatus.BAD_REQUEST, "TODOLIST004", "마감 시간이 지난 투두리스트입니다."),
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
