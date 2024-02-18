package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AlbumErrorCode implements ErrorCodeInterface {

    EMPTY_ALBUM(HttpStatus.NOT_FOUND, "ALBUM001", "존재하지 않는 앨범입니다."),
    NO_AUTHORIZATION_ALBUM(HttpStatus.BAD_REQUEST, "ALBUM002", "해당 앨범에 대한 수정 권한이 없습니다."),
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
