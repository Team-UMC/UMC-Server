package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AlbumCommentErrorCode implements ErrorCodeInterface {

    EMPTY_ALBUM_COMMENT(HttpStatus.NOT_FOUND,"COMMENT001","존재하지 않는 댓글입니다."),
    NO_AUTHORIZATION_ALBUM_COMMENT(HttpStatus.BAD_REQUEST, "COMMENT002", "해당 댓글에 대한 수정 권한이 없습니다."),
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
