package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCodeInterface {

    EMPTY_MEMBER(HttpStatus.CONFLICT, "MEMBER001", "존재하지 않는 사용자입니다."),
    UNAUTHORIZED_UPDATE_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER002", "상위 운영진의 직책을 수정할 수 없습니다."),
    UNAUTHORIZED_UPDATE_CENTER_POSITION(HttpStatus.BAD_REQUEST, "MEMBER003", "지부, 학교 운영진은 중앙 직책을 부여할 수 없습니다."),
    EMPTY_MEMBER_UNIVERSITY(HttpStatus.CONFLICT, "MEMBER004", "소속 대학교가 존재하지 않는 사용자입니다."),
    UNAUTHENTICATED_GITHUB(HttpStatus.BAD_REQUEST, "MEMBER005", "깃허브 연동이 완료되지 않은 사용자입니다."),
    INVALID_MEMBER_KEYWORD(HttpStatus.BAD_REQUEST, "MEMBER006", "검색어 양식[닉네임/이름]에 맞추어 작성해주세요. ex) 벡스/김준석"),
    NO_SEMESTER_PARTS(HttpStatus.BAD_REQUEST, "MEMBER007", "참여한 기수와 파트가 존재하지 않는 사용자입니다."),
    DUPLICATED_GIT_NICKNAME(HttpStatus.BAD_REQUEST, "MEMBER008", "중복된 깃허브 닉네임입니다."),
    NO_PERMISSION_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER009", "권한이 없는 사용자입니다."),
    NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST, "MEMBER0010", "포인트가 부족합니다.")

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
