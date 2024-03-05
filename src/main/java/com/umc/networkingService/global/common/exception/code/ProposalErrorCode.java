package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ProposalErrorCode implements ErrorCodeInterface {

    EMPTY_PROPOSAL(HttpStatus.BAD_REQUEST, "PROPOSAL001", "존재하지 않는 건의글입니다."),
    NO_UPDATE_AUTHORIZATION_PROPOSAL(HttpStatus.BAD_REQUEST, "PROPOSAL002", "해당 건의글에 대한 수정 권한이 없습니다."),
    NO_DELETE_AUTHORIZATION_PROPOSAL(HttpStatus.BAD_REQUEST, "PROPOSAL003", "해당 건의글에 대한 삭제 권한이 없습니다.");

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
