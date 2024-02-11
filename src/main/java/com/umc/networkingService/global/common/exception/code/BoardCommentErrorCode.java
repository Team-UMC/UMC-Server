package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardCommentErrorCode implements ErrorCodeInterface {

    EMPTY_BOARD_COMMENT(HttpStatus.NOT_FOUND,"COMMENT001","댓글을 찾을 수 없습니다."),
    NO_AUTHORIZATION_BOARD_COMMENT(HttpStatus.BAD_REQUEST,"COMMENT002", "댓글을 작성/수정/삭제 할 권한이 없습니다."),
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
